package gr.codehub.team2.Sacchon.resource.Reporter.impl;

import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.model.Patient;
import gr.codehub.team2.Sacchon.repository.PatientRepository;
import gr.codehub.team2.Sacchon.repository.util.JpaUtil;
import gr.codehub.team2.Sacchon.representation.PatientRepresentation;
import gr.codehub.team2.Sacchon.resource.Reporter.PatientListNoActivityResource;
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

public class PatientListNoActivityResourceImpl extends ServerResource implements PatientListNoActivityResource {

    public static final Logger LOGGER = Engine.getLogger(PatientListNoActivityResourceImpl.class);
    private PatientRepository patientRepository;
    private EntityManager em;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Initialization of patient repository and parsing dates from front end.
     */
    @Override
    protected void doInit() {
        LOGGER.info("Initialising DoctorListNoActivity resource starts");
        try {
            em = JpaUtil.getEntityManager();
            patientRepository = new PatientRepository(em);
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
    protected void doRelease() {
        em.close();
    }

    /** This method is used by Chief Doctors in order to get Patients that didn't add Measurements in a specific Date range.
     *
     * @return List of Patients Representation in JSON Format
     * @throws NotFoundException
     */
    @Override
    public List<PatientRepresentation> getPatientsNoActivity() throws NotFoundException {
        LOGGER.finer("Get Patients with no activity");
        ResourceUtils.checkRole(this, CustomRole.ROLE_CHIEF_DOCTOR.getRoleName());
        LOGGER.finer("Chief doctor allowed to see the patients with no activity");
        try {
            List<Patient> patientsAll = patientRepository.findAll();
            List<Patient> patients = patientRepository.findPatientsWithActivity(startDate, endDate);
            patientsAll.removeAll(patients);
            List<PatientRepresentation> patientRepresentationList = new ArrayList<>();
            patientsAll.forEach(patient -> patientRepresentationList.add(PatientRepresentation.getPatientRepresentation(patient)));
            LOGGER.finer("Patients with no activity found");
            return patientRepresentationList;
        } catch (Exception e) {
            LOGGER.finer("Patients with no activity not found");
            throw new NotFoundException("Patients with no activity not found");
        }
    }


}
