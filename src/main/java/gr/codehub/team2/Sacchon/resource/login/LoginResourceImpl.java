package gr.codehub.team2.Sacchon.resource.login;

import gr.codehub.team2.Sacchon.exceptions.BadEntityException;
import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.repository.ApplicationUserRepository;
import gr.codehub.team2.Sacchon.repository.util.JpaUtil;
import gr.codehub.team2.Sacchon.representation.ApplicationUserRepresentation;
import gr.codehub.team2.Sacchon.resource.util.ResourceValidator;
import gr.codehub.team2.Sacchon.security.dao.ApplicationUser;
import org.restlet.engine.Engine;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.logging.Logger;

public class LoginResourceImpl extends ServerResource implements LoginResource {
    public static final Logger LOGGER = Engine.getLogger(LoginResourceImpl.class);

    private ApplicationUserRepository applicationUserRepository;
    private EntityManager em;

    /**
     * Initialization of Application User repository.
     */
    @Override
    protected void doInit() {
        LOGGER.finer("Initialising login resource starts");
        try {
            em = JpaUtil.getEntityManager();
            applicationUserRepository = new ApplicationUserRepository(em);
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

    /** This method is used in order an Application User can login to the system.
     *
     * @param aUserRepresentation
     * @return Application User Representation in JSON format.
     * @throws NotFoundException
     * @throws BadEntityException
     */
    @Override
    public ApplicationUserRepresentation loginUser(ApplicationUserRepresentation aUserRepresentation) throws NotFoundException, BadEntityException {
        LOGGER.finer("User trying to login");
        ResourceValidator.notNull(aUserRepresentation);
        LOGGER.finer("Credentials checked");
        try {
            if (applicationUserRepository.findUserLogin(aUserRepresentation.getUsername(), aUserRepresentation.getPassword()).isPresent()) {
                LOGGER.finer("User found");
                Optional<ApplicationUser> applicationUserOptional = applicationUserRepository.findUserLogin(aUserRepresentation.getUsername(), aUserRepresentation.getPassword());
                ApplicationUser applicationUser = applicationUserOptional.get();
                ApplicationUserRepresentation applicationUserRepresentation = ApplicationUserRepresentation.getApplicationUserRepresentation(applicationUser);
                LOGGER.finer("User able to login");
                return applicationUserRepresentation;
            }
        } catch (Exception e) {
            LOGGER.finer("User not found");
            throw new NotFoundException("User not found");
        }
        return null;
    }
}
