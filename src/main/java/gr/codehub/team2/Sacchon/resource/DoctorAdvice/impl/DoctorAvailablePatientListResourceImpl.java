package gr.codehub.team2.Sacchon.resource.DoctorAdvice.impl;

import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.model.Patient;
import gr.codehub.team2.Sacchon.repository.PatientRepository;
import gr.codehub.team2.Sacchon.repository.util.JpaUtil;
import gr.codehub.team2.Sacchon.representation.PatientRepresentation;
import gr.codehub.team2.Sacchon.resource.DoctorAdvice.DoctorPatientListResource;
import gr.codehub.team2.Sacchon.resource.util.ResourceUtils;
import gr.codehub.team2.Sacchon.security.CustomRole;
import org.restlet.engine.Engine;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DoctorAvailablePatientListResourceImpl extends ServerResource implements DoctorPatientListResource {
    public static final Logger LOGGER = Engine.getLogger(DoctorAvailablePatientListResourceImpl.class);
    private PatientRepository patientRepository;
    private EntityManager em;
    /**
     * Initialization of repositories.
     */
    @Override
    protected void doInit() {
        LOGGER.info("Initialising Doctor Patient resource starts");
        try {
            em = JpaUtil.getEntityManager();
            patientRepository = new PatientRepository(em);
        } catch (Exception ex) {
            throw new ResourceException(ex);
        }
        LOGGER.info("Initialising Doctor Patient resource ends");
    }
    /**
     * Release method that closes entity manager.
     */
    @Override
    protected void doRelease() {
        em.close();
    }

    /** This method is used from Doctors in order to get available Patients List.
     *
     * @return Patients List Representation in JSON Format.
     * @throws NotFoundException
     */
    @Override
    public List<PatientRepresentation> getPatients() throws NotFoundException {
        LOGGER.finer("Trying to get available patients.");
        ResourceUtils.checkRole(this, CustomRole.ROLE_DOCTOR.getRoleName());
        LOGGER.finer("Doctor allowed to see available patients.");
        try {
            List<Patient> patients = patientRepository.findAvailablePatients();
            List<PatientRepresentation> patientRepresentationList = new ArrayList<>();
            patients.forEach(patient -> patientRepresentationList.add(PatientRepresentation.getPatientRepresentation(patient)));
            return patientRepresentationList;
        } catch (Exception e) {
            LOGGER.finer("Available patients not found");
            throw new NotFoundException("Available patients not found");
        }
    }
}
