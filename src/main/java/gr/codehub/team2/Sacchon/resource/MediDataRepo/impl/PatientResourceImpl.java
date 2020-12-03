package gr.codehub.team2.Sacchon.resource.MediDataRepo.impl;

import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.model.Doctor;
import gr.codehub.team2.Sacchon.model.Patient;
import gr.codehub.team2.Sacchon.repository.ApplicationUserRepository;
import gr.codehub.team2.Sacchon.repository.DoctorRepository;
import gr.codehub.team2.Sacchon.repository.PatientRepository;
import gr.codehub.team2.Sacchon.repository.util.JpaUtil;
import gr.codehub.team2.Sacchon.representation.PatientRepresentation;
import gr.codehub.team2.Sacchon.resource.MediDataRepo.PatientResource;
import gr.codehub.team2.Sacchon.resource.util.ResourceUtils;
import gr.codehub.team2.Sacchon.security.CustomRole;
import gr.codehub.team2.Sacchon.security.dao.ApplicationUser;
import org.restlet.engine.Engine;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PatientResourceImpl extends ServerResource implements PatientResource {

    public static final Logger LOGGER = Engine.getLogger(PatientResourceImpl.class);
    private PatientRepository patientRepository;
    private ApplicationUserRepository applicationUserRepository;
    private EntityManager em;
    private long pid;

    /**
     * Initialization of repositories and parsing Patient id from front end.
     */
    @Override
    protected void doInit() {
        try {
            em = JpaUtil.getEntityManager();
            patientRepository = new PatientRepository(em);
            applicationUserRepository = new ApplicationUserRepository(em);
            pid = Long.parseLong(getAttribute("patient_id"));
        } catch (Exception ex) {
            throw new ResourceException(ex);
        }
    }
    /**
     * This release method closes the entityManager
     */
    @Override
    protected void doRelease() {
        em.close();
    }

    /** This method is used by Patients in order to get their details.
     *
     * @return Patient Representation in JSON Format.
     * @throws NotFoundException
     * @throws ResourceException
     */
    @Override
    public PatientRepresentation getPatient() throws NotFoundException, ResourceException {
        LOGGER.finer("Get a patient");
        ResourceUtils.checkRole(this, CustomRole.ROLE_PATIENT.getRoleName());
        Optional<Patient> patient = patientRepository.findById(pid);
        setExisting(patient.isPresent());
        if (!patient.isPresent()) throw new NotFoundException("Patient is not found");
        PatientRepresentation patientRepresentation = PatientRepresentation.getPatientRepresentation(patient.get());
        return patientRepresentation;
    }

    /** This method is used from Patients to remove their accounts. They become inactive in database.
     * Also their accounts become inactive as we need their consultations and measurements to stay in database untouched
     * for reporting purposes.
     * If sometime they need to get their account back they can use it again after database manipulation.
     *
     * @throws NotFoundException
     * @throws ResourceException
     */
    @Override
    public void removePatient() throws NotFoundException, ResourceException {
        LOGGER.finer("Remove a patient");
        ResourceUtils.checkRole(this, CustomRole.ROLE_PATIENT.getRoleName());
        LOGGER.finer("Patient allowed to remove his account");
        try {
            Optional<Patient> patientOptional = patientRepository.findById(pid);
            if (!patientOptional.isPresent()) throw new NotFoundException("Patient is not existing");
            Patient patient = patientOptional.get();
            patient.setActive(false);
            patient.setDoctor(null);
            patientRepository.save(patient);
            LOGGER.finer("Patient is inactive and removed connection with doctor");
            Optional<ApplicationUser> applicationUserOptional = applicationUserRepository.findPatientUser(pid);
            if (!applicationUserOptional.isPresent()) throw new NotFoundException("Application User is not existing");
            ApplicationUser applicationUser = applicationUserOptional.get();
            applicationUser.setActive(false);
            applicationUserRepository.save(applicationUser);
            LOGGER.finer("Patient removed his account");
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error when removing a patient", ex);
            throw new ResourceException(ex);
        }
    }

//    @Override
//    public PatientRepresentation updatePatient(PatientRepresentation patientRepresentationIn) throws NotFoundException, BadEntityException {
//        LOGGER.finer("Update a patient");
//        ResourceUtils.checkRole(this, CustomRole.ROLE_PATIENT.getRoleName());
//        LOGGER.finer("Patient allowed to update his account");
//        ResourceValidator.notNull(patientRepresentationIn);
//        ResourceValidator.validatePatient(patientRepresentationIn);
//        LOGGER.finer("Patient checked");
//        try{
//            Optional<Patient> patientOptional = patientRepository.findById(pid);
//            if(!patientOptional.isPresent()) throw new NotFoundException("Patient is not existing");
//            Patient patient = patientOptional.get();
//            patient.setName(patientRepresentationIn.getName());
//            patient.setDob(patientRepresentationIn.getDob());
//            patient.setGender(patientRepresentationIn.getGender());
//            patient.setDiabetesType(patientRepresentationIn.getDiabetesType());
//            patient.setDateDiagnosed(patientRepresentationIn.getDateDiagnosed());
//            patient.setUsername(patientRepresentationIn.getUsername());
//            patient.setPassword(patientRepresentationIn.getPassword());
//            patientRepository.save(patient);
//            LOGGER.finer("Patient changes saved");
//            return PatientRepresentation.getPatientRepresentation(patient);
//        }
//        catch (Exception ex){
//            LOGGER.log(Level.WARNING, "Error when making changes to a patient", ex);
//            throw new ResourceException(ex);
//        }
//    }
}
