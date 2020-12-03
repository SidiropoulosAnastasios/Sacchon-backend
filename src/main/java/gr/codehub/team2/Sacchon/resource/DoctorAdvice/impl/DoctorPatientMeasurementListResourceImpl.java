package gr.codehub.team2.Sacchon.resource.DoctorAdvice.impl;

import gr.codehub.team2.Sacchon.exceptions.ForbiddenException;
import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.model.Measurement;
import gr.codehub.team2.Sacchon.model.Patient;
import gr.codehub.team2.Sacchon.repository.MeasurementRepository;
import gr.codehub.team2.Sacchon.repository.PatientRepository;
import gr.codehub.team2.Sacchon.repository.util.JpaUtil;
import gr.codehub.team2.Sacchon.representation.MeasurementRepresentation;
import gr.codehub.team2.Sacchon.resource.DoctorAdvice.DoctorPatientMeasurementListResource;
import gr.codehub.team2.Sacchon.resource.util.ResourceUtils;
import gr.codehub.team2.Sacchon.security.CustomRole;
import org.restlet.engine.Engine;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class DoctorPatientMeasurementListResourceImpl extends ServerResource implements DoctorPatientMeasurementListResource {

    public static final Logger LOGGER = Engine.getLogger(DoctorPatientMeasurementListResourceImpl.class);
    private MeasurementRepository measurementRepository;
    private EntityManager em;
    private PatientRepository patientRepository;
    private long pid;
    private long did;
    /**
     * Release method that closes entity manager.
     */
    @Override
    protected void doRelease() {
        em.close();
    }
    /**
     * Initialization of repositories and parsing ids from front end.
     */
    @Override
    protected void doInit() {

        LOGGER.info("Initialising measurement list resource starts");
        try {
            em = JpaUtil.getEntityManager();
            measurementRepository = new MeasurementRepository(em);
            patientRepository = new PatientRepository(em);
            pid = Long.parseLong(getAttribute("patient_id"));
            did = Long.parseLong(getAttribute("doctor_id"));
        } catch (Exception ex) {
            throw new ResourceException(ex);
        }
        LOGGER.info("Initialising measurement list resource ends");
    }

    /** This method is used from Doctors in order to get the Measurements from one of their Patients.
     *
     * @return Measurement List Representation in JSON Format.
     * @throws NotFoundException
     * @throws ForbiddenException
     */
    @Override
    public List<MeasurementRepresentation> getMeasurements() throws NotFoundException, ForbiddenException {
        LOGGER.finer("User trying to see patient measurements");
        ResourceUtils.checkRole(this, CustomRole.ROLE_DOCTOR.getRoleName());
        LOGGER.finer("Doctor trying to see patient measurements");
        if (!patientRepository.patientAndDoctorConnection(pid, did)) {
            throw new ForbiddenException("This doctor does not consult this patient.");
        }
        LOGGER.finer("Doctor can see measurements of his patient");
        try {
            List<Measurement> measurements = measurementRepository.findMyMeasurements(pid);
            List<MeasurementRepresentation> measurementRepresentationList = new ArrayList<>();
            measurements.forEach(measurement -> measurementRepresentationList.add(MeasurementRepresentation.getMeasurementRepresentation(measurement)));
            LOGGER.finer("Measurements found");
            return measurementRepresentationList;
        } catch (Exception e) {
            throw new NotFoundException("measurements not found");
        }
    }

}
