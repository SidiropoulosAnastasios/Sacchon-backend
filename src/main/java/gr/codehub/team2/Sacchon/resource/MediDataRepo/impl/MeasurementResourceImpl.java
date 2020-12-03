package gr.codehub.team2.Sacchon.resource.MediDataRepo.impl;

import gr.codehub.team2.Sacchon.exceptions.BadEntityException;
import gr.codehub.team2.Sacchon.exceptions.ForbiddenException;
import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.model.Measurement;
import gr.codehub.team2.Sacchon.model.Patient;
import gr.codehub.team2.Sacchon.repository.MeasurementRepository;
import gr.codehub.team2.Sacchon.repository.PatientRepository;
import gr.codehub.team2.Sacchon.repository.util.JpaUtil;
import gr.codehub.team2.Sacchon.representation.MeasurementRepresentation;
import gr.codehub.team2.Sacchon.resource.MediDataRepo.MeasurementResource;
import gr.codehub.team2.Sacchon.resource.util.ResourceUtils;
import gr.codehub.team2.Sacchon.resource.util.PatientResourceValidator;
import gr.codehub.team2.Sacchon.security.CustomRole;
import org.restlet.engine.Engine;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MeasurementResourceImpl extends ServerResource implements MeasurementResource {

    public static final Logger LOGGER = Engine.getLogger(MeasurementResourceImpl.class);
    private MeasurementRepository measurementRepository;
    private PatientRepository patientRepository;
    private EntityManager em;
    private long pid;
    private long mid;

    /**
     * Initialization of repositories and parsing ids from front end.
     */
    @Override
    protected void doInit() {
        LOGGER.info("Initialising measurement resource starts");
        try {
            em = JpaUtil.getEntityManager();
            measurementRepository = new MeasurementRepository(em);
            patientRepository = new PatientRepository(em);
            pid = Long.parseLong(getAttribute("patient_id"));
            mid = Long.parseLong(getAttribute("measurement_id"));
        } catch (Exception ex) {

            throw new ResourceException(ex);
        }
        LOGGER.info("Initialising measurement resource ends");

    }
    /**
     * Release method that closes entity manager.
     */
    @Override
    protected void doRelease() {
        em.close();
    }

    /** This method is used by Patients in order to get a specific Measurement.
     *
     * @return Measurement Representation in JSON Format.
     * @throws NotFoundException
     * @throws ResourceException
     * @throws ForbiddenException
     */
    @Override
    public MeasurementRepresentation getMeasurement() throws NotFoundException, ResourceException, ForbiddenException {
        LOGGER.finer("Trying to get a measurement");
        ResourceUtils.checkRole(this, CustomRole.ROLE_PATIENT.getRoleName());
        Optional<Patient> patientOptional = patientRepository.findById(pid);
        if (!patientOptional.isPresent()) throw new NotFoundException("Patient is not found");
        LOGGER.finer("Patient allowed to get a measurement.");
        Optional<Measurement> measurementOptional = measurementRepository.findById(mid);
        if (!measurementOptional.isPresent()) throw new NotFoundException("Measurement is not found");
        LOGGER.finer("Measurement with this id is available:" + mid);
        MeasurementRepresentation measurementRepresentation = MeasurementRepresentation.getMeasurementRepresentation(measurementOptional.get());
        return measurementRepresentation;
    }

    /** This method is used by Patients in order to remove a specific Measurement.
     *
     * @throws NotFoundException
     * @throws ResourceException
     * @throws ForbiddenException
     */
    @Override
    public void removeMeasurement() throws NotFoundException, ResourceException, ForbiddenException {
        LOGGER.finer("Trying to remove a measurement");
        ResourceUtils.checkRole(this, CustomRole.ROLE_PATIENT.getRoleName());
        Optional<Patient> patientOptional = patientRepository.findById(pid);
        if (!patientOptional.isPresent()) throw new NotFoundException("Patient is not found");
        LOGGER.finer("Patient allowed to remove a measurement.");
        try {
            Boolean isDeleted = measurementRepository.deleteById(mid);
            if (!isDeleted) {
                LOGGER.finer("Measurement id does not exist");
                throw new NotFoundException("Measurement with this id doesn't exist:" + mid);
            }
            LOGGER.finer("Measurement with the following id successfully removed:" + mid);
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error when removing a measurement", ex);
            throw new ResourceException(ex);
        }
    }

    /** This method is used by Patients in order to update an existing Measurement.
     *
     * @param measurementRepresentationIn
     * @return Measurement Representation in JSON Format.
     * @throws NotFoundException
     * @throws BadEntityException
     * @throws ForbiddenException
     */
    @Override
    public MeasurementRepresentation updateMeasurement(MeasurementRepresentation measurementRepresentationIn) throws NotFoundException, BadEntityException, ForbiddenException {
        LOGGER.finer("Update a measurement");
        ResourceUtils.checkRole(this, CustomRole.ROLE_PATIENT.getRoleName());
        Optional<Patient> patientOptional = patientRepository.findById(pid);
        if (!patientOptional.isPresent()) throw new NotFoundException("Patient is not found");
        LOGGER.finer("Patient allowed to update his measurement");
        PatientResourceValidator.notNull(measurementRepresentationIn);
        PatientResourceValidator.validateMeasurement(measurementRepresentationIn);
        LOGGER.finer("Measurement checked");
        try {
            Optional<Measurement> measurementOptional = measurementRepository.findById(mid);
            if (!measurementOptional.isPresent()) throw new NotFoundException("Measurement is not existing");
            Measurement measurement = measurementOptional.get();
            measurement.setDate(LocalDate.parse(measurementRepresentationIn.getDate()));
            measurement.setGlucoseLevel(measurementRepresentationIn.getGlucoseLevel());
            measurement.setCarbIntake(measurementRepresentationIn.getCarbIntake());
            measurementRepository.save(measurement);
            LOGGER.finer("Measurement changes saved");
            return MeasurementRepresentation.getMeasurementRepresentation(measurement);
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error when making changes to a measurement", ex);
            throw new ResourceException(ex);
        }
    }

}