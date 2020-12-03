package gr.codehub.team2.Sacchon.resource.DoctorAdvice.impl;

import gr.codehub.team2.Sacchon.exceptions.BadEntityException;
import gr.codehub.team2.Sacchon.exceptions.ForbiddenException;
import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.model.Consultation;
import gr.codehub.team2.Sacchon.model.Doctor;
import gr.codehub.team2.Sacchon.model.Patient;
import gr.codehub.team2.Sacchon.repository.ConsultationRepository;
import gr.codehub.team2.Sacchon.repository.DoctorRepository;
import gr.codehub.team2.Sacchon.repository.PatientRepository;
import gr.codehub.team2.Sacchon.repository.util.JpaUtil;
import gr.codehub.team2.Sacchon.representation.ConsultationRepresentation;
import gr.codehub.team2.Sacchon.resource.DoctorAdvice.ConsultationListResource;
import gr.codehub.team2.Sacchon.resource.util.ResourceUtils;
import gr.codehub.team2.Sacchon.resource.util.ConsultationResourceValidator;
import gr.codehub.team2.Sacchon.security.CustomRole;
import org.restlet.engine.Engine;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsultationListResourceImpl extends ServerResource implements ConsultationListResource {
    public static final Logger LOGGER = Engine.getLogger(ConsultationResourceImpl.class);

    private ConsultationRepository consultationRepository;
    private EntityManager em;
    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;
    private boolean hasDid;
    private long did;
    private long pid;

    /**
     * Initialization of repositories and parsing ids from front end.
     */
    @Override
    protected void doInit() {
        LOGGER.info("Initialising consultation resource starts");
        try {
            em = JpaUtil.getEntityManager();
            consultationRepository = new ConsultationRepository(em);
            patientRepository = new PatientRepository(em);
            doctorRepository = new DoctorRepository(em);
            pid = Long.parseLong(getAttribute("patient_id"));
            try {
                did = Long.parseLong(getAttribute("doctor_id"));
                hasDid = true;
            } catch (Exception ex) {
                hasDid = false;
            }
        } catch (Exception ex) {
            throw new ResourceException(ex);
        }
        LOGGER.info("Initialising consultation resource ends");
    }

    /**
     * Release method that closes entity manager.
     */
    @Override
    protected void doRelease() {
        em.close();
    }

    /** This method is used in order to find Consultations for a Patient.
     *
     * @return List of Consultation Representations in JSON format.
     * @throws NotFoundException
     * @throws ForbiddenException
     */
    public List<ConsultationRepresentation> getConsultations() throws NotFoundException, ForbiddenException {
        LOGGER.finer("Select all consultations.");
        List<String> roles = new ArrayList<>();
        roles.add(CustomRole.ROLE_PATIENT.getRoleName());
        roles.add(CustomRole.ROLE_DOCTOR.getRoleName());
        ResourceUtils.checkRoles(this, roles);

        //if the user that tries to see the consultation is a doctor
        if (hasDid) {
            LOGGER.finer("Doctor trying to get consultation list");
            if (!patientRepository.patientAndDoctorConnection(pid, did)) {
                throw new ForbiddenException("This doctor does not consult this patient.");
            }
        }
        try {
            List<Consultation> consultations = consultationRepository.findConsultationsByPatient(pid);
            List<ConsultationRepresentation> consultationRepresentationList = new ArrayList<>();
            consultations.forEach(consultation -> consultationRepresentationList.add(ConsultationRepresentation.getConsultationRepresentation(consultation)));
            LOGGER.finer("Patient consultations list found");
            return consultationRepresentationList;
        } catch (Exception e) {
            throw new NotFoundException("Consultations not found");
        }
    }

    /** This method is used by the Doctor in order to add a Consultation in a specific Patient.
     * We check if Patient is active and is ready to be consulted. Also we check if this Patient
     * belongs to this Doctor or is not attached in another Doctor. Only then a Doctor can add
     * a Consultation.
     *
     * @param consultationRepresentationIn
     * @return Consultation Representation in JSON Format.
     * @throws BadEntityException
     */
    @Override
    public ConsultationRepresentation addConsultation(ConsultationRepresentation consultationRepresentationIn) throws BadEntityException {
        LOGGER.finer("Add a new consultation.");
        // Check authorization
        ResourceUtils.checkRole(this, CustomRole.ROLE_DOCTOR.getRoleName());
        LOGGER.finer("Doctor allowed to add a consultation.");
        // Check entity
        ConsultationResourceValidator.notNull(consultationRepresentationIn);
        ConsultationResourceValidator.validate(consultationRepresentationIn);
        LOGGER.finer("Consultation checked");
        try {
            Optional<Doctor> doctorOptional = doctorRepository.findById(did);
            if (!doctorOptional.isPresent())  throw new NotFoundException("Patient is not found");
            Doctor doctor = doctorOptional.get();
            LOGGER.finer("Doctor found");
            Optional<Patient> patientOptional = patientRepository.findById(pid);
            if (!patientOptional.isPresent())  throw new NotFoundException("Patient is not found");
            Patient patient = patientOptional.get();
            LOGGER.finer("Patient found");
            if (patient.isActive() && patient.isReadyForConsultation() && ((patient.getDoctor() == null) || (patient.getDoctor() == doctor))) {
                Consultation consultation = ConsultationRepresentation.getConsultation(consultationRepresentationIn, doctorOptional, patientOptional);
                consultation.setReadByPatient(false);
                patient.setReadyForConsultation(false);
                patient.setDoctor(doctor);
                patientRepository.save(patient);
                consultationRepository.save(consultation);
                LOGGER.finer("Consultation successfully added.");
                return ConsultationRepresentation.getConsultationRepresentation(consultation);
            } else
                throw new NotFoundException("");
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error when adding a consultation", ex);
            throw new ResourceException(ex);
        }
    }

}


