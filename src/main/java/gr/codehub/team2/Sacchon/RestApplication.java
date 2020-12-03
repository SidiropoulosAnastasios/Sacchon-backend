package gr.codehub.team2.Sacchon;

import gr.codehub.team2.Sacchon.repository.util.JpaUtil;
import gr.codehub.team2.Sacchon.router.CustomRouter;
import gr.codehub.team2.Sacchon.security.CustomRole;
import gr.codehub.team2.Sacchon.security.Shield;
import gr.codehub.team2.Sacchon.security.cors.CustomCorsFilter;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.Role;

import javax.persistence.EntityManager;
import java.util.logging.Logger;

public class RestApplication extends Application {


    public static final Logger LOGGER = Engine.getLogger(RestApplication.class);


    public static void main(String[] args) throws Exception {
        LOGGER.info("Contacts application starting...");

        EntityManager em = JpaUtil.getEntityManager();
        em.close();


        Component c = new Component();
        c.getServers().add(Protocol.HTTP, 9000);
        c.getDefaultHost().attach("/sacchon", new RestApplication());
        c.start();

        LOGGER.info("Sample Web API started");
        LOGGER.info("URL: http://localhost:9000/sacchon/");

    }

    public RestApplication() {

        setName("WebAPITutorial");
        setDescription("Full Web API tutorial");

        getRoles().add(new Role(this, CustomRole.ROLE_CHIEF_DOCTOR.getRoleName()));
        getRoles().add(new Role(this, CustomRole.ROLE_DOCTOR.getRoleName()));
        getRoles().add(new Role(this, CustomRole.ROLE_PATIENT.getRoleName()));

    }

    @Override
    public Restlet createInboundRoot() {

        CustomRouter customRouter = new CustomRouter(this);
        Shield shield = new Shield(this);

        Router publicRouter = customRouter.publicResources();
        ChallengeAuthenticator apiGuard = shield.createApiGuard();
        // Create the api router, protected by a guard

        Router apiRouter = customRouter.createApiRouter();
        apiGuard.setNext(apiRouter);

        publicRouter.attachDefault(apiGuard);

        // return publicRouter;

        CustomCorsFilter corsFilter = new CustomCorsFilter(this);
        return corsFilter.createCorsFilter(publicRouter);


    }

}
