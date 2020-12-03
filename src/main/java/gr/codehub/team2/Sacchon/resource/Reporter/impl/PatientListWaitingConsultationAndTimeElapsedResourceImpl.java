package gr.codehub.team2.Sacchon.resource.Reporter.impl;

import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.model.Patient;
import gr.codehub.team2.Sacchon.repository.ConsultationRepository;
import gr.codehub.team2.Sacchon.repository.MeasurementRepository;
import gr.codehub.team2.Sacchon.repository.PatientRepository;
import gr.codehub.team2.Sacchon.repository.util.JpaUtil;
import gr.codehub.team2.Sacchon.representation.PatientRepresentation;
import gr.codehub.team2.Sacchon.resource.Reporter.PatientListWaitingConsultationAndTimeElapsedResource;
import gr.codehub.team2.Sacchon.resource.util.ResourceUtils;
import gr.codehub.team2.Sacchon.security.CustomRole;
import org.restlet.engine.Engine;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static java.time.temporal.ChronoUnit.DAYS;

public class PatientListWaitingConsultationAndTimeElapsedResourceImpl extends ServerResource implements PatientListWaitingConsultationAndTimeElapsedResource {

    public static final Logger LOGGER = Engine.getLogger(PatientListWaitingConsultationAndTimeElapsedResourceImpl.class);
    private PatientRepository patientRepository;
    private ConsultationRepository consultationRepository;
    private MeasurementRepository measurementRepository;
    private EntityManager em;
    private int CALENDAR_MONTH = 30;
    private long timeElapsed = 0;

    /**
     * Initialization of repositories.
     */
    @Override
    protected void doInit() {

        LOGGER.info("Initialising patient list waiting consultation and time elapsed resource starts");
        try {
            em = JpaUtil.getEntityManager();
            patientRepository = new PatientRepository(em);
            consultationRepository = new ConsultationRepository(em);
            measurementRepository = new MeasurementRepository(em);
        } catch (Exception ex) {
            throw new ResourceException(ex);
        }
        LOGGER.info("Initialising patient list waiting consultation and time elapsed resource ends");
    }

    /**
     * This release method closes the entityManager
     */
    @Override
    protected void doRelease() {
        em.close();
    }

    /**
     * THis method is used by Chief Doctors to get the Patients that wait for a consultation and the time elapsed since
     * they were ready to get one. We find those patients that are ready to get a consultation. After that we check if they had
     * not one consultation in the past. If they didn't, we count the days since the last measurement and we subtract
     * one calendar month(30 days) to count the final time elapsed
     * For those that had a consultation we count the days since the last consultation.
     *
     * @return List of Patients and time elapsed Representation in JSON Format.
     * @throws NotFoundException
     */
    @Override
    public List<PatientRepresentation> getPatientsWaitingConsultationAndTimeElapsed() throws NotFoundException {
        ResourceUtils.checkRole(this, CustomRole.ROLE_CHIEF_DOCTOR.getRoleName());
        LOGGER.finer("Chief doctor permitted to see the patients who are waiting for a consultation and the time elapsed.");
        List<Patient> patients = patientRepository.getPatientsWaitingConsultation();
        LOGGER.finer("Patients and the time elapsed waiting for a consultation has started");
        List<PatientRepresentation> resultList = new ArrayList<>();
        for (Patient patient : patients) {
            LocalDate latestConsultationDate = consultationRepository.findLatestConsultationDateByPatient(patient.getId());
            if (latestConsultationDate == null) {
                LOGGER.finer("Patient has not been consulted earlier");
                LocalDate firstMeasurementDate = measurementRepository.findFirstDateOfPatientsMeasurements(patient.getId());
                long daysBetweenFirstMeasurement = DAYS.between(firstMeasurementDate, LocalDate.now());
                if (daysBetweenFirstMeasurement > CALENDAR_MONTH) {
                    timeElapsed = daysBetweenFirstMeasurement; // - CALENDAR_MONTH;
                    LOGGER.finer("Checking the time elapsed of the patients who are waiting for a consultation");
                }
            } else {
                LOGGER.finer("Patient has been consulted earlier");
                long daysBetween = DAYS.between(latestConsultationDate, LocalDate.now());
                timeElapsed = daysBetween;// - CALENDAR_MONTH;
                LOGGER.finer("Checking the time elapsed of the patients who are waiting for a consultation");
            }
            LOGGER.finer("Checking the time elapsed of the patient who are waiting for a consultation is finished.");
            resultList.add(PatientRepresentation.getPatientsList(patient, timeElapsed));
        }
        LOGGER.finer("Patients and the time elapsed waiting for a consultation has finished");
        return resultList;
    }
}
