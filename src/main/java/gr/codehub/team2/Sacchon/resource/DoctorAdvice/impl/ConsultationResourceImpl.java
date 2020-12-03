package gr.codehub.team2.Sacchon.resource.DoctorAdvice.impl;

import gr.codehub.team2.Sacchon.exceptions.BadEntityException;
import gr.codehub.team2.Sacchon.exceptions.ForbiddenException;
import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.model.Consultation;
import gr.codehub.team2.Sacchon.model.Patient;
import gr.codehub.team2.Sacchon.repository.ConsultationRepository;
import gr.codehub.team2.Sacchon.repository.DoctorRepository;
import gr.codehub.team2.Sacchon.repository.PatientRepository;
import gr.codehub.team2.Sacchon.repository.util.JpaUtil;
import gr.codehub.team2.Sacchon.representation.ConsultationRepresentation;
import gr.codehub.team2.Sacchon.resource.DoctorAdvice.ConsultationResource;
import gr.codehub.team2.Sacchon.resource.util.ResourceUtils;
import gr.codehub.team2.Sacchon.security.CustomRole;
import org.restlet.engine.Engine;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class ConsultationResourceImpl extends ServerResource implements ConsultationResource {

    public static final Logger LOGGER = Engine.getLogger(ConsultationResourceImpl.class);
    private PatientRepository patientRepository;
    private ConsultationRepository consultationRepository;
    private DoctorRepository doctorRepository;
    private EntityManager em;
    private long pid;
    private long cid;
    private long did;
    private boolean hasDid;
    /**
     * Initialization of repositories and parsing ids by front.
     */
    @Override
    protected void doInit() {
        try {
            em = JpaUtil.getEntityManager();
            consultationRepository = new ConsultationRepository(em);
            cid = Long.parseLong(getAttribute("consultation_id"));
            patientRepository = new PatientRepository(em);
            pid = Long.parseLong(getAttribute("patient_id"));
            doctorRepository = new DoctorRepository(em);
//            did = Long.parseLong(getAttribute("doctor_id"));
           // try {
                did = doctorRepository.findUserDoctorId();
           //     hasDid = true;
           // } catch (Exception ex) {
            //    hasDid = false;
           // }
        } catch (Exception ex) {
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

    /** This method is used in order to get a specific Consultation from a Doctor or a Patient.
     *
     * @return Consultation Representation in JSON Format.
     * @throws NotFoundException
     * @throws ResourceException
     * @throws ForbiddenException
     */
    @Override
    public ConsultationRepresentation getConsultation() throws NotFoundException, ResourceException, ForbiddenException {
        LOGGER.finer("Trying to get a consultation");
        List<String> roles = new ArrayList<>();
        roles.add(CustomRole.ROLE_PATIENT.getRoleName());
        roles.add(CustomRole.ROLE_DOCTOR.getRoleName());
        ResourceUtils.checkRoles(this, roles);
        if (hasDid) {
            LOGGER.finer("Doctor trying to get a consultation");
            if (patientRepository.patientAndDoctorConnection(pid, did)) {
                LOGGER.finer("Doctor consulting patient trying to get a consultation");
                Optional<Consultation> consultationOptional = consultationRepository.findById(cid);
                if (!consultationOptional.isPresent()) throw new NotFoundException("Consultation is not found");
                ConsultationRepresentation consultationRepresentation = ConsultationRepresentation.getConsultationRepresentation(consultationOptional.get());
                return consultationRepresentation;
            } else {
                throw new ForbiddenException("This doctor does not consult this patient.");
            }
        } else {
            LOGGER.finer("Patient trying to get a consultation");
            Optional<Consultation> consultationOptional = consultationRepository.findById(cid);
            if (!consultationOptional.isPresent()) throw new NotFoundException("Consultation is not found");
            ConsultationRepresentation consultationRepresentation = ConsultationRepresentation.getConsultationRepresentation(consultationOptional.get());
            return consultationRepresentation;
        }
    }

    /** This method is used for the update of an existing Consultation when the Consultation is not read by Patient yet.
     *
     * @param consultationRepresentationIn
     * @return Consultation Representation in JSON Format.
     * @throws NotFoundException
     * @throws BadEntityException
     * @throws ForbiddenException
     */
    @Override
    public ConsultationRepresentation updateConsultation(ConsultationRepresentation consultationRepresentationIn) throws NotFoundException, BadEntityException, ForbiddenException {
        LOGGER.finer("Trying to update a consultation");
        ResourceUtils.checkRole(this, CustomRole.ROLE_DOCTOR.getRoleName());
        LOGGER.finer("Doctor allowed to update a consultation");
        Optional<Consultation> consultationOpt = consultationRepository.findById(cid);
        if (!consultationOpt.isPresent()) throw new NotFoundException("This Consultation doesn't exists");
        Consultation consultation = consultationOpt.get();
        if (!consultation.isReadByPatient()) {
            consultation.setMedication(consultationRepresentationIn.getMedication());
            consultation.setDosage(consultationRepresentationIn.getDosage());
            consultation.setExtraInfo(consultationRepresentationIn.getExtraInfo());
            consultation.setDate(LocalDate.now());
            consultationRepository.save(consultation);
            return ConsultationRepresentation.getConsultationRepresentation(consultation);
        }
        else {
            throw new ForbiddenException(" Consultation can't be updated");
        }


    }
}