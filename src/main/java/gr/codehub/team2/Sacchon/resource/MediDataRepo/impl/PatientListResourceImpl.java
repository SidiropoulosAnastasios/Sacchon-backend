package gr.codehub.team2.Sacchon.resource.MediDataRepo.impl;

import gr.codehub.team2.Sacchon.exceptions.BadEntityException;
import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.model.Patient;
import gr.codehub.team2.Sacchon.repository.PatientRepository;
import gr.codehub.team2.Sacchon.repository.util.JpaUtil;
import gr.codehub.team2.Sacchon.representation.PatientRepresentation;
import gr.codehub.team2.Sacchon.resource.MediDataRepo.PatientListResource;
import gr.codehub.team2.Sacchon.resource.util.ResourceUtils;
import gr.codehub.team2.Sacchon.security.CustomRole;
import org.restlet.engine.Engine;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PatientListResourceImpl extends ServerResource implements PatientListResource {

    public static final Logger LOGGER = Engine.getLogger(PatientListResourceImpl.class);
    public PatientRepository patientRepository;
    private EntityManager em;

    /**
     * Initialization of patient repository.
     */
    @Override
    protected void doInit() {
        LOGGER.finer("Initialising patient resource starts");
        try {
            em = JpaUtil.getEntityManager();
            patientRepository = new PatientRepository(em);
        } catch (Exception ex) {
            throw new ResourceException(ex);
        }
        LOGGER.finer("Initialising patient resource ends");
    }

    /**
     * This release method closes the entityManager
     */
    @Override
    protected void doRelease() {
        em.close();
    }

    /** This method is used by Chief Doctors in order to see all existing Patients.
     *
     * @return Patient List Representation in JSON Format.
     * @throws ResourceException
     * @throws NotFoundException
     */
    @Override
    public List<PatientRepresentation> getPatients() throws ResourceException, NotFoundException {
        LOGGER.finer("Select all patients.");
        ResourceUtils.checkRole(this, CustomRole.ROLE_CHIEF_DOCTOR.getRoleName());
        LOGGER.finer("Doctor allowed to see patients.");
        try {
            List<Patient> patients = patientRepository.findAll();
            List<PatientRepresentation> patientRepresentationList = new ArrayList<>();
            patients.forEach(patient -> patientRepresentationList.add(PatientRepresentation.getPatientRepresentation(patient)));
            LOGGER.finer("Patients found");
            return patientRepresentationList;
        }
        catch (Exception e){
            LOGGER.finer("Patients not found");
            throw new NotFoundException("Patients not found");
        }
    }
}
