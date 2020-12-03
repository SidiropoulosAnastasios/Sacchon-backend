package gr.codehub.team2.Sacchon.resource.DoctorAdvice.impl;

import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.model.Patient;
import gr.codehub.team2.Sacchon.repository.DoctorRepository;
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

public class DoctorPatientListResourceImpl extends ServerResource implements DoctorPatientListResource {
    public static final Logger LOGGER = Engine.getLogger(DoctorPatientListResourceImpl.class);
    public PatientRepository patientRepository;

    private EntityManager em;
    private long did;
    /**
     * Initialization of repositories and parsing ids from front end.
     */
    @Override
    protected void doInit() {
        LOGGER.info("Initialising Doctor Patient resource starts");
        try {
            em = JpaUtil.getEntityManager();
            patientRepository = new PatientRepository(em);
            did = Long.parseLong(getAttribute("doctor_id"));
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

    /** This method is used from Doctors in order to get their Patients List.
     *
     * @return Patients List Representation in JSON Format.
     * @throws NotFoundException
     */
    @Override
    public List<PatientRepresentation> getPatients() throws NotFoundException {
        LOGGER.finer("Trying to get patients of Doctor id:" + did);
        ResourceUtils.checkRole(this, CustomRole.ROLE_DOCTOR.getRoleName());
        LOGGER.finer("Doctor allowed to see his patients.");
        try {
            List<Patient> patients = patientRepository.findMyPatients(did);
            List<PatientRepresentation> patientRepresentationList = new ArrayList<>();
            patients.forEach(patient -> patientRepresentationList.add(PatientRepresentation.getPatientRepresentation(patient)));
            LOGGER.finer("Patients found");
            return patientRepresentationList;
        } catch (Exception e) {
            LOGGER.finer("Patients not found");
            throw new NotFoundException("Patients not found");
        }
    }
}
