package gr.codehub.team2.Sacchon.resource.register.impl;

import gr.codehub.team2.Sacchon.exceptions.BadEntityException;
import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.model.Patient;
import gr.codehub.team2.Sacchon.repository.ApplicationUserRepository;
import gr.codehub.team2.Sacchon.repository.PatientRepository;
import gr.codehub.team2.Sacchon.repository.util.JpaUtil;
import gr.codehub.team2.Sacchon.representation.ApplicationUserRepresentation;
import gr.codehub.team2.Sacchon.representation.UserPatientRepresentation;
import gr.codehub.team2.Sacchon.resource.register.RegisterResource;
import gr.codehub.team2.Sacchon.security.dao.ApplicationUser;
import org.restlet.engine.Engine;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.persistence.EntityManager;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterResourceImpl extends ServerResource implements RegisterResource {
    public static final Logger LOGGER = Engine.getLogger(RegisterResourceImpl.class);

    private ApplicationUserRepository applicationUserRepository;
    private PatientRepository patientRepository;
    private EntityManager em;
    /**
     * Initialization of repositories.
     */
    @Override
    protected void doInit() {
        LOGGER.finer("Initialising login resource starts");
        try {
            em = JpaUtil.getEntityManager();
            applicationUserRepository = new ApplicationUserRepository(em);
            patientRepository = new PatientRepository(em);
        } catch (Exception ex) {
            throw new ResourceException(ex);
        }
        LOGGER.finer("Initialising login resource ends");
    }
    /**
     * Release method that closes entity manager.
     */
    @Override
    protected void doRelease() {
        em.close();
    }

    /** This method is used in order Patients can register for an account.
     * Their registration inputs are divided in two tables.
     *
     * @param userPatientRepresentation
     * @return Application User Representation in JSON Format.
     * @throws NotFoundException
     * @throws BadEntityException
     */
    public ApplicationUserRepresentation registerUserPatient(UserPatientRepresentation userPatientRepresentation) throws NotFoundException, BadEntityException {
        LOGGER.finer("Add new patient user.");
        if (userPatientRepresentation == null) throw new BadEntityException("bad entity");
        LOGGER.finer("User checked");
        LOGGER.finer("patientIn checked");
        try {
            Patient patient = UserPatientRepresentation.getUserPatient(userPatientRepresentation);
            patient.setActive(true);
            patient.setReadyForConsultation(false);
            patientRepository.save(patient);
            LOGGER.finer("Patient successfully added.");
            ApplicationUser applicationUser = UserPatientRepresentation.getUser(userPatientRepresentation);
            applicationUser.setPatient(patient);
            applicationUser.setDoctor(null);
            applicationUser.setChiefDoctor(null);
            applicationUserRepository.save(applicationUser);
            LOGGER.finer("User successfully added.");
            return ApplicationUserRepresentation.getApplicationUserRepresentation(applicationUser);
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error when adding a patient", ex);
            throw new ResourceException(ex);
        }
    }
}
