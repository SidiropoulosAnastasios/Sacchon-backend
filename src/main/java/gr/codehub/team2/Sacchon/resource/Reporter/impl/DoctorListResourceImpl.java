package gr.codehub.team2.Sacchon.resource.Reporter.impl;

import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.model.Doctor;
import gr.codehub.team2.Sacchon.repository.DoctorRepository;
import gr.codehub.team2.Sacchon.repository.PatientRepository;
import gr.codehub.team2.Sacchon.repository.util.JpaUtil;
import gr.codehub.team2.Sacchon.representation.DoctorRepresentation;
import gr.codehub.team2.Sacchon.resource.Reporter.DoctorListResource;
import gr.codehub.team2.Sacchon.resource.util.ResourceUtils;
import gr.codehub.team2.Sacchon.security.CustomRole;
import org.restlet.engine.Engine;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DoctorListResourceImpl extends ServerResource implements DoctorListResource {


    public static final Logger LOGGER = Engine.getLogger(DoctorListResourceImpl.class);
    private EntityManager em;
    private DoctorRepository doctorRepository;

    /**
     * Initialization of doctor repository.
     */
    @Override
    protected void doInit() {
        LOGGER.finer("Initialising doctor list resource starts");
        try {
            em = JpaUtil.getEntityManager();
            doctorRepository = new DoctorRepository(em);
        } catch (Exception ex) {
            throw new ResourceException(ex);
        }
        LOGGER.finer("Initialising doctor list resource ends");
    }

    /**
     * This release method closes the entityManager
     */
    @Override
    protected void doRelease() {
        em.close();
    }

    /** This method is used from Chief Doctors to get a List of all Doctors.
     *
     * @return List of Doctors Representation in JSON Format.
     * @throws ResourceException
     * @throws NotFoundException
     */
    @Override
    public List<DoctorRepresentation> getDoctors() throws ResourceException,NotFoundException {
        LOGGER.finer("Select all Doctors");
        ResourceUtils.checkRole(this, CustomRole.ROLE_CHIEF_DOCTOR.getRoleName());
        LOGGER.finer("Chief doctor allowed to see doctors.");
        try {
            List<Doctor> doctors = doctorRepository.findAll();
            List<DoctorRepresentation> doctorRepresentationsList = new ArrayList<>();
            doctors.forEach(doctor -> doctorRepresentationsList.add(DoctorRepresentation.getDoctorRepresentation(doctor)));
            LOGGER.finer("Doctors found");
            return doctorRepresentationsList;
        }
        catch (Exception e){
            LOGGER.finer("Doctors not found");
            throw new NotFoundException("Doctors not found");
        }
    }
}
