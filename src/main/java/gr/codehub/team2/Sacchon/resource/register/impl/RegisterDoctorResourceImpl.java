package gr.codehub.team2.Sacchon.resource.register.impl;

import gr.codehub.team2.Sacchon.exceptions.BadEntityException;
import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.model.Doctor;
import gr.codehub.team2.Sacchon.repository.ApplicationUserRepository;
import gr.codehub.team2.Sacchon.repository.DoctorRepository;
import gr.codehub.team2.Sacchon.repository.util.JpaUtil;
import gr.codehub.team2.Sacchon.representation.ApplicationUserRepresentation;
import gr.codehub.team2.Sacchon.representation.UserDoctorRepresentation;
import gr.codehub.team2.Sacchon.resource.register.RegisterDoctorResource;
import gr.codehub.team2.Sacchon.security.dao.ApplicationUser;
import org.restlet.engine.Engine;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.persistence.EntityManager;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterDoctorResourceImpl extends ServerResource implements RegisterDoctorResource {
    public static final Logger LOGGER = Engine.getLogger(RegisterDoctorResourceImpl.class);

    private ApplicationUserRepository applicationUserRepository;
    private DoctorRepository doctorRepository;
    private EntityManager em;
    private Long did;
    /**
     * Initialization of repositories.
     */
    @Override
    protected void doInit() {
        LOGGER.finer("Initialising login resource starts");
        try {
            em = JpaUtil.getEntityManager();
            applicationUserRepository = new ApplicationUserRepository(em);
            doctorRepository = new DoctorRepository(em);
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

    /** This method is used in order Doctors can register for an account.
     * We made the assumption that Doctors can register giving a specific Permission Code.
     * Their registration inputs are divided in two tables.
     *
     * @param userDoctorRepresentation
     * @return Application User Representation in JSON Format.
     * @throws NotFoundException
     * @throws BadEntityException
     * @throws ResourceException
     */
    @Override
    public ApplicationUserRepresentation registerUserDoctor(UserDoctorRepresentation userDoctorRepresentation) throws NotFoundException, BadEntityException, ResourceException {
        LOGGER.finer("Add new patient doctor.");
        if (userDoctorRepresentation == null) throw new BadEntityException("bad entity");
        LOGGER.finer("User checked");
        LOGGER.finer("Doctor checked");
        try {
            Doctor doctor = userDoctorRepresentation.getUserDoctor(userDoctorRepresentation);
            doctor.setActive(true);
            doctorRepository.save(doctor);
            LOGGER.finer("Doctor successfully updated to database.");
            ApplicationUser applicationUser = userDoctorRepresentation.getUser(userDoctorRepresentation);
            applicationUser.setDoctor(doctor);
            applicationUser.setPatient(null);
            applicationUser.setChiefDoctor(null);
            applicationUserRepository.save(applicationUser);
            LOGGER.finer("User successfully added to database.");
            return ApplicationUserRepresentation.getApplicationUserRepresentation(applicationUser);

        } catch (
                Exception ex) {
            LOGGER.log(Level.WARNING, "Error when adding a doctor", ex);
            throw new ResourceException(ex);
        }
    }
}
