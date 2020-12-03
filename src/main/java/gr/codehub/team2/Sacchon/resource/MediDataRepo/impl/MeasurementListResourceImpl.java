package gr.codehub.team2.Sacchon.resource.MediDataRepo.impl;

import gr.codehub.team2.Sacchon.exceptions.BadEntityException;
import gr.codehub.team2.Sacchon.exceptions.ForbiddenException;
import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.model.Measurement;
import gr.codehub.team2.Sacchon.model.Patient;
import gr.codehub.team2.Sacchon.repository.ConsultationRepository;
import gr.codehub.team2.Sacchon.repository.MeasurementRepository;
import gr.codehub.team2.Sacchon.repository.PatientRepository;
import gr.codehub.team2.Sacchon.repository.util.JpaUtil;
import gr.codehub.team2.Sacchon.representation.MeasurementRepresentation;
import gr.codehub.team2.Sacchon.resource.MediDataRepo.MeasurementListResource;
import gr.codehub.team2.Sacchon.resource.util.ResourceUtils;
import gr.codehub.team2.Sacchon.resource.util.MeasurementResourceValidator;
import gr.codehub.team2.Sacchon.security.CustomRole;

import org.restlet.engine.Engine;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.time.temporal.ChronoUnit.DAYS;

public class MeasurementListResourceImpl extends ServerResource implements MeasurementListResource {

    public static final Logger LOGGER = Engine.getLogger(MeasurementListResourceImpl.class);
    private MeasurementRepository measurementRepository;
    private ConsultationRepository consultationRepository;
    private EntityManager em;
    private PatientRepository patientRepository;
    private long pid;
    private long CALENDAR_MONTH = 30;

    @Override
    protected void doRelease() {
        em.close();
    }

    @Override
    protected void doInit() {

        LOGGER.info("Initialising measurement list resource starts");
        try {
            em = JpaUtil.getEntityManager();
            measurementRepository = new MeasurementRepository(em);
            patientRepository = new PatientRepository(em);
            consultationRepository = new ConsultationRepository(em);
            pid = Long.parseLong(getAttribute("patient_id"));
        } catch (Exception ex) {
            throw new ResourceException(ex);
        }
        LOGGER.info("Initialising measurement list resource ends");
    }


    @Override
    public List<MeasurementRepresentation> getMeasurements() throws NotFoundException, ForbiddenException {
//        if(patientRepository.patientAndDoctorConnection(pid, did)){
//            throw new ForbiddenException("This doctor does not consult this patient.");
//        }
        LOGGER.finer("Select all measurements");
//        List<String> roles = new ArrayList<>();
//        roles.add(CustomRole.ROLE_PATIENT.getRoleName());
//        roles.add(CustomRole.ROLE_DOCTOR.getRoleName());
//        ResourceUtils.checkRoles(this, roles);
        ResourceUtils.checkRole(this, CustomRole.ROLE_PATIENT.getRoleName());
        LOGGER.finer("Patient can see his measurements");
        try {
            List<Measurement> measurements = measurementRepository.findMyMeasurements(pid);
            List<MeasurementRepresentation> measurementRepresentationList = new ArrayList<>();
            measurements.forEach(measurement -> measurementRepresentationList.add(MeasurementRepresentation.getMeasurementRepresentation(measurement)));
            LOGGER.finer("Measurements found");
            return measurementRepresentationList;
        } catch (Exception e) {
            throw new NotFoundException("Measurements not found");
        }
    }

    @Override
    public MeasurementRepresentation addMeasurement(MeasurementRepresentation measurementIn) throws ResourceException, BadEntityException {
        LOGGER.finer("Add a new measurement.");
        ResourceUtils.checkRole(this, CustomRole.ROLE_PATIENT.getRoleName());
        LOGGER.finer("Patient allowed to add a measurement.");
        MeasurementResourceValidator.notNull(measurementIn);
        MeasurementResourceValidator.validate(measurementIn);
        LOGGER.finer("measurementRepresentationIn checked");
        try {
            Optional<Patient> patient = patientRepository.findById(pid);
            LOGGER.finer("Patient found");
            Measurement measurement = MeasurementRepresentation.getMeasurement(measurementIn, patient);
            measurementRepository.save(measurement);
            LOGGER.finer("Measurement successfully added.");
            LOGGER.finer("Check if patient is ready for consultation has started");
            LocalDate latestConsultationDate = consultationRepository.findLatestConsultationDateByPatient(pid);
            if(latestConsultationDate != null) {
                LOGGER.finer("Patient has been consulted before - He is not new to the system");
                long daysBetween = DAYS.between(latestConsultationDate, LocalDate.now());
                if(daysBetween > CALENDAR_MONTH) {
                    LOGGER.finer("Patient is ready for consultation");
                    patient.get().setReadyForConsultation(true);
                    patientRepository.save(patient.get());
                }
            } else {
                LOGGER.finer("Patient has never been consulted before - He is new to the system");
                LocalDate firstMeasurementDate = measurementRepository.findFirstDateOfPatientsMeasurements(pid);
                long daysBetweenFirstMeasurement = DAYS.between(firstMeasurementDate, LocalDate.now());
                if(daysBetweenFirstMeasurement > CALENDAR_MONTH) {
                    LOGGER.finer("Patient is ready for consultation");
                    patient.get().setReadyForConsultation(true);
                    patientRepository.save(patient.get());
                }
            }
            LOGGER.finer("Check if patient is ready for consultation is has ended");
            return MeasurementRepresentation.getMeasurementRepresentation(measurement);
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error when adding a measurement", ex);
            throw new ResourceException(ex);
        }
    }

}