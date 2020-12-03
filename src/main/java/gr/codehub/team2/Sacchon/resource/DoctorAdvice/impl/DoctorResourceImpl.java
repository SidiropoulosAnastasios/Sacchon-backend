package gr.codehub.team2.Sacchon.resource.DoctorAdvice.impl;

import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.model.Doctor;
import gr.codehub.team2.Sacchon.model.Patient;
import gr.codehub.team2.Sacchon.repository.ApplicationUserRepository;
import gr.codehub.team2.Sacchon.repository.DoctorRepository;
import gr.codehub.team2.Sacchon.repository.PatientRepository;
import gr.codehub.team2.Sacchon.repository.util.JpaUtil;
import gr.codehub.team2.Sacchon.representation.DoctorRepresentation;
import gr.codehub.team2.Sacchon.resource.DoctorAdvice.DoctorResource;
import gr.codehub.team2.Sacchon.resource.util.ResourceUtils;
import gr.codehub.team2.Sacchon.security.CustomRole;
import gr.codehub.team2.Sacchon.security.dao.ApplicationUser;
import org.restlet.engine.Engine;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DoctorResourceImpl extends ServerResource implements DoctorResource {

    public static final Logger LOGGER = Engine.getLogger(DoctorResourceImpl.class);
    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;
    private ApplicationUserRepository applicationUserRepository;
    private EntityManager em;
    private long did;
    /**
     * Initialization of repositories and parsing ids from front end.
     */
    @Override
    protected void doInit() {
        LOGGER.info("Initialising DoctorResource resource starts");
        try {
            em = JpaUtil.getEntityManager();
            doctorRepository = new DoctorRepository(em);
            patientRepository =  new PatientRepository(em);
            applicationUserRepository = new ApplicationUserRepository(em);
            did = Long.parseLong(getAttribute("doctor_id"));

        } catch (Exception ex) {
            throw new ResourceException(ex);
        }
        LOGGER.info("Initialising DoctorResource has ended");
    }
    /**
     * Release method that closes entity manager.
     */
    @Override
    protected void doRelease() {
        em.close();
    }

    /** This method is used from Doctors to get their details.
     *
     * @return Doctor details Representation in JSON Format.
     * @throws NotFoundException
     * @throws ResourceException
     */
    @Override
    public DoctorRepresentation getDoctor() throws NotFoundException, ResourceException {
        ResourceUtils.checkRole(this, CustomRole.ROLE_DOCTOR.getRoleName());
        Optional<Doctor> doctor = doctorRepository.findById(did);
        setExisting(doctor.isPresent());
        if (!doctor.isPresent()) throw new NotFoundException("Doctor is not found");
        DoctorRepresentation doctorRepresentation = DoctorRepresentation.getDoctorRepresentation(doctor.get());
        return doctorRepresentation;
    }

    /** This method is used from Doctors to remove their accounts. They become inactive in database.
     * Also their accounts become inactive as we need their consultations to stay in database untouched.
     * If sometime they need to get their account back they can use it again after database manipulation.
     *
     * @throws NotFoundException
     * @throws ResourceException
     */
    @Override
    public void removeDoctor() throws NotFoundException, ResourceException {
        LOGGER.finer("Remove a doctor");
        ResourceUtils.checkRole(this, CustomRole.ROLE_DOCTOR.getRoleName());
        LOGGER.finer("Permission granted");
        try {
            Optional<Doctor> doctorOptional = doctorRepository.findById(did);
            if (!doctorOptional.isPresent()) throw new NotFoundException("Doctor is not existing");
            Doctor doctor = doctorOptional.get();
            doctor.setActive(false);
            doctorRepository.save(doctor);
            Optional<ApplicationUser> applicationUserOptional = applicationUserRepository.findDoctorUser(did);
            if (!applicationUserOptional.isPresent()) throw new NotFoundException("Application User is not existing");
            ApplicationUser applicationUser = applicationUserOptional.get();
            applicationUser.setActive(false);
            applicationUserRepository.save(applicationUser);
            LOGGER.finer("Doctor removed his account");
            List<Patient> patients = patientRepository.findMyPatients(did);
            patients.forEach(patient -> patient.setDoctor(null));
            patients.forEach(patient -> patientRepository.save(patient));
            LOGGER.finer("Doctor removed from Patients");
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error when removing a doctor", ex);
            throw new ResourceException(ex);
        }
    }

//    @Override
//    public DoctorRepresentation updateDoctor(DoctorRepresentation doctorRepresentationIn) throws NotFoundException, BadEntityException {
//       ResourceUtils.checkRole(this,CustomRole.ROLE_DOCTOR.getRoleName());
//        Optional<Doctor> doctorOpt = doctorRepository.findById(id);
//        if (!doctorOpt.isPresent()) throw new NotFoundException("This Doctor doesn't exists");
//        Doctor doctor = doctorOpt.get();
//
//        doctor.setAfm(doctorRepresentationIn.getAfm());
//        doctor.setName(doctorRepresentationIn.getName());
//        doctor.setPassword(doctorRepresentationIn.getPassword());
//        doctor.setUsername(doctorRepresentationIn.getUsername());
//        doctor.setRole(doctorRepresentationIn.getRole());
//        doctor.setDoctorPermissionCode(doctorRepresentationIn.getDoctorPermissionCode());
//        doctor.setActive(doctorRepresentationIn.isActive());
//
//        doctorRepository.save(doctor);
//        return DoctorRepresentation.getDoctorRepresentation(doctor);
//    }
}
