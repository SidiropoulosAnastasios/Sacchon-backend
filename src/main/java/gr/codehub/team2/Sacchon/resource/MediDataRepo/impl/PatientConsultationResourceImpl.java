package gr.codehub.team2.Sacchon.resource.MediDataRepo.impl;

import gr.codehub.team2.Sacchon.exceptions.ForbiddenException;
import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.model.Consultation;
import gr.codehub.team2.Sacchon.model.Patient;
import gr.codehub.team2.Sacchon.repository.ConsultationRepository;
import gr.codehub.team2.Sacchon.repository.PatientRepository;
import gr.codehub.team2.Sacchon.repository.util.JpaUtil;
import gr.codehub.team2.Sacchon.representation.ConsultationRepresentation;
import gr.codehub.team2.Sacchon.resource.MediDataRepo.PatientConsultationResource;
import gr.codehub.team2.Sacchon.resource.util.ResourceUtils;
import gr.codehub.team2.Sacchon.security.CustomRole;
import org.restlet.engine.Engine;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.logging.Logger;

public class PatientConsultationResourceImpl extends ServerResource implements PatientConsultationResource {

    public static final Logger LOGGER = Engine.getLogger(PatientConsultationResourceImpl.class);
    private PatientRepository patientRepository;
    private ConsultationRepository consultationRepository;
    private EntityManager em;
    private long pid;
    private long cid;
    /**
     * Initialization of repositories and parsing ids from front end.
     */
    @Override
    protected void doInit() {
        try {
            em = JpaUtil.getEntityManager();
            consultationRepository = new ConsultationRepository(em);
            cid = Long.parseLong(getAttribute("consultation_id"));
            patientRepository = new PatientRepository(em);
            pid = Long.parseLong(getAttribute("patient_id"));
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

    /** This method is used by Patients in order to get an existing Consultation.
     *
     * @return Consultation Representation in JSON Format.
     * @throws NotFoundException
     * @throws ResourceException
     * @throws ForbiddenException
     */
    @Override
    public ConsultationRepresentation getConsultation() throws NotFoundException, ResourceException, ForbiddenException {
        LOGGER.finer("Trying to get a consultation");
        ResourceUtils.checkRole(this, CustomRole.ROLE_PATIENT.getRoleName());
        Optional<Patient> patient = patientRepository.findById(pid);
        setExisting(patient.isPresent());
        if (!patient.isPresent())  throw new NotFoundException("Patient is not found");
        //if (!patient.get().isActive()) throw new ForbiddenException("Patient is not allowed to get the consultation");
        LOGGER.finer("Patient allowed to get a consultation.");
        // Show if consultation belongs to patient id & doctor id
        Optional<Consultation> consultation = consultationRepository.findById(cid);
        setExisting(consultation.isPresent());
        if (!consultation.isPresent())  throw new NotFoundException("Consultation is not found");

        //change value of field readByPatient to true cause Patient saw the Consultation
        //consultation.map(Consultation::setReadByPatient(true));

        ConsultationRepresentation consultationRepresentation = ConsultationRepresentation.getConsultationRepresentation(consultation.get());
        return consultationRepresentation;
    }
}
