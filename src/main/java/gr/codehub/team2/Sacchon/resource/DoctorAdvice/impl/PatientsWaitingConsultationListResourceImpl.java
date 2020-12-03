package gr.codehub.team2.Sacchon.resource.DoctorAdvice.impl;

import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.model.Patient;
import gr.codehub.team2.Sacchon.repository.DoctorRepository;
import gr.codehub.team2.Sacchon.repository.PatientRepository;
import gr.codehub.team2.Sacchon.repository.util.JpaUtil;
import gr.codehub.team2.Sacchon.representation.PatientRepresentation;
import gr.codehub.team2.Sacchon.resource.DoctorAdvice.DoctorPatientListResource;
import gr.codehub.team2.Sacchon.resource.util.ResourceUtils;
import gr.codehub.team2.Sacchon.security.CustomRole;
import org.restlet.engine.Engine;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PatientsWaitingConsultationListResourceImpl extends ServerResource implements DoctorPatientListResource {
    public static final Logger LOGGER = Engine.getLogger(PatientsWaitingConsultationListResourceImpl.class);
    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;
    private EntityManager em;
    private long did;
    /**
     * Initialization of repositories and parsing ids from front end.
     */
    @Override
    protected void doInit() {
        LOGGER.info("Initialising Doctor Patient resource starts");
        try {
            em = JpaUtil.getEntityManager();
            doctorRepository = new DoctorRepository(em);
            patientRepository = new PatientRepository(em);
            did = Long.parseLong(getAttribute("doctor_id"));
        } catch (Exception ex) {

            throw new ResourceException(ex);
        }
        LOGGER.info("Initialising Doctor Patient resource ends");
    }
    /**
     * Release method that closes entity manager.
     */
    @Override
    protected void doRelease() {
        em.close();
    }

    /** This method is used from Doctors in order to get a List of Patients that wait for a consultation.
     *
     * @return Patients List Representation in JSON Format.
     * @throws NotFoundException
     */
    @Override
    public List<PatientRepresentation> getPatients() throws NotFoundException {
        LOGGER.finer("Trying to get patients waiting consultation");
        ResourceUtils.checkRole(this, CustomRole.ROLE_DOCTOR.getRoleName());
        LOGGER.finer("Doctor allowed to see patients waiting consultation.");
        try {
            List<Patient> patients = patientRepository.findPatientsWaitingConsultation(did);
            List<PatientRepresentation> patientRepresentationList = new ArrayList<>();
            patients.forEach(patient -> patientRepresentationList.add(PatientRepresentation.getPatientRepresentation(patient)));
            LOGGER.finer("Patients waiting consultation found");
            return patientRepresentationList;
        } catch (Exception e) {
            LOGGER.finer("Patients waiting consultation not found");
            throw new NotFoundException("Patients waiting consultation not found");
        }
    }
}
