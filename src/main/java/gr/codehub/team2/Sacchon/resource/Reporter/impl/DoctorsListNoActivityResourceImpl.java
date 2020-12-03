package gr.codehub.team2.Sacchon.resource.Reporter.impl;

import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.model.Doctor;
import gr.codehub.team2.Sacchon.repository.DoctorRepository;
import gr.codehub.team2.Sacchon.repository.util.JpaUtil;
import gr.codehub.team2.Sacchon.representation.DoctorRepresentation;
import gr.codehub.team2.Sacchon.resource.Reporter.DoctorsListNoActivityResource;
import gr.codehub.team2.Sacchon.resource.util.ResourceUtils;
import gr.codehub.team2.Sacchon.security.CustomRole;
import org.restlet.engine.Engine;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DoctorsListNoActivityResourceImpl extends ServerResource implements DoctorsListNoActivityResource {

    public static final Logger LOGGER = Engine.getLogger(DoctorsListNoActivityResourceImpl.class);
    private DoctorRepository doctorRepository;
    private EntityManager em;
    private LocalDate startDate;
    private LocalDate endDate;
    /**
     * Initialization of doctor repository and parsing dates from front end.
     */
    @Override
    protected void doInit() {
        LOGGER.info("Initialising DoctorListNoActivity resource starts");
        try {
            em = JpaUtil.getEntityManager();
            doctorRepository = new DoctorRepository(em);
            startDate = LocalDate.parse(getAttribute("start_date"));
            endDate = LocalDate.parse(getAttribute("end_date"));

        } catch (Exception e) {
            throw new ResourceException(e);
        }
        LOGGER.info("Initialising DoctorListNoActivity resource has ended");
    }

    /**
     * This release method closes the entityManager
     */
    @Override
    protected void doRelease(){
        em.close();
    }

    /** This method is used by Chief Doctors in order to get a List of Doctors that didn't serve a consultation
     *  in a specific Date range.
     *
     * @return List of Doctors Representation in JSON Format.
     * @throws NotFoundException
     */
    @Override
    public List<DoctorRepresentation> getDoctorsNoActivity() throws NotFoundException {
        LOGGER.finer("Get Doctors with no activity");
        ResourceUtils.checkRole(this, CustomRole.ROLE_CHIEF_DOCTOR.getRoleName());
        LOGGER.finer("Chief doctor allowed to see the doctors with no activity");

        try {
            List<Doctor> doctorsAll =doctorRepository.findAll();
            List<Doctor> doctors = doctorRepository.findDoctorsActivity(startDate, endDate);
            doctorsAll.remove(doctors);
            List<DoctorRepresentation> doctorRepresentationList = new ArrayList<>();
            doctorsAll.forEach(doctor -> doctorRepresentationList.add(DoctorRepresentation.getDoctorRepresentation(doctor)));
            LOGGER.finer("Doctors with no activity found");
            return doctorRepresentationList;
        }
        catch (Exception e){
            LOGGER.finer("Doctors with no activity not found");
            throw new NotFoundException("Doctors with no activity not found");
        }
    }

}
