package gr.codehub.team2.Sacchon.resource.Reporter.impl;

import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.model.ChiefDoctor;
import gr.codehub.team2.Sacchon.repository.ChiefDoctorRepository;
import gr.codehub.team2.Sacchon.repository.util.JpaUtil;
import gr.codehub.team2.Sacchon.representation.*;
import gr.codehub.team2.Sacchon.resource.Reporter.ChiefDoctorResource;
import gr.codehub.team2.Sacchon.resource.util.ResourceUtils;
import gr.codehub.team2.Sacchon.security.CustomRole;
import org.restlet.engine.Engine;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.logging.Logger;

public class ChiefDoctorResourceImpl extends ServerResource implements ChiefDoctorResource {

    public static final Logger LOGGER = Engine.getLogger(ChiefDoctorResourceImpl.class);
    private ChiefDoctorRepository chiefDoctorRepository ;
    private EntityManager em;
    private long rid;
    /**
     * Initialization of repositories and parsing id from front end.
     */
    @Override
    protected void doInit() {
        try {
            em = JpaUtil.getEntityManager();
            chiefDoctorRepository = new ChiefDoctorRepository(em);
            rid = Long.parseLong(getAttribute("reporter_id"));
        }
        catch(Exception ex){

            throw new ResourceException(ex);

        }
    }
    /**
     * Release method that closes entity manager.
     */
    @Override
    protected void doRelease() {
        em.close();
    }

    /** This method is used by Chief Doctors in order to get their details.
     *
     * @return Chief Doctor Representation in JSON Format.
     * @throws NotFoundException
     * @throws ResourceException
     */
    @Override
    public ChiefDoctorRepresentation getChiefDoctor() throws NotFoundException,ResourceException {
        LOGGER.finer("Get Chief Doctors");
        ResourceUtils.checkRole(this, CustomRole.ROLE_CHIEF_DOCTOR.getRoleName());
        LOGGER.finer("Chief doctors are allowed to see the chief doctors");
        Optional<ChiefDoctor> chiefDoctor = chiefDoctorRepository.findById(rid);
        setExisting(chiefDoctor.isPresent());
        if (!chiefDoctor.isPresent())  throw new NotFoundException("Chief Doctor is not found");
        ChiefDoctorRepresentation chiefDoctorRepresentation = ChiefDoctorRepresentation.getChiefDoctorRepresentation(chiefDoctor.get());
        return chiefDoctorRepresentation;
    }

}