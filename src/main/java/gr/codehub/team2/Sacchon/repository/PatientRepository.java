package gr.codehub.team2.Sacchon.repository;

import gr.codehub.team2.Sacchon.model.Patient;
import gr.codehub.team2.Sacchon.repository.lib.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class PatientRepository extends Repository<Patient, Long> {

    private EntityManager entityManager;

    public PatientRepository(EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public Class<Patient> getEntityClass() {
        return Patient.class;
    }

    @Override
    public String getEntityClassName() {
        return Patient.class.getName();
    }

    /**
     * Finds active Patients of a specific Doctor using Doctor ID.
     * @param did
     * @return List of active Patients.
     */
    public List<Patient> findMyPatients(Long did) {
        List<Patient> ps = entityManager.createQuery("from Patient p WHERE p.doctor.id = :did AND p.active = 1")
                .setParameter("did", did)
                .getResultList();

        return ps;
    }

    /**
     * Finds all new active Patients that have not be consulted yet from a Doctor and are waiting for a consultation to be given.
     * @return List of Patients.
     */
    public List<Patient> findAvailablePatients() {
        List<Patient> ps = entityManager.createQuery("from Patient p WHERE (p.doctor IS NULL) AND p.readyForConsultation = 1 AND p.active = 1")
                .getResultList();
        return ps;
    }

    /**
     * Finds all active Patients that wait for consultation from a Doctor using Doctor ID and are waiting for a consultation to be given.
     * @param did
     * @return List of Patients.
     */
    public List<Patient> findPatientsWaitingConsultation(Long did) {
        List<Patient> patientList = entityManager.createQuery("from Patient p WHERE p.readyForConsultation = 1 AND p.active = 1 AND (p.doctor IS NULL OR p.doctor.id = :did)")
                .setParameter("did", did)
                .getResultList();
        return patientList;
    }

    /**
     * Finds Patients that have activity in a specific date range.
     * @param startDate
     * @param endDate
     * @return List of Patients.
     */
    public List<Patient> findPatientsWithActivity(LocalDate startDate, LocalDate endDate) {
        List<Patient> ps = entityManager.createQuery("SELECT DISTINCT p from Patient p INNER JOIN Measurement m ON p.id = m.patient.id WHERE (m.date>=:startDate and m.date<=:endDate)")
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();

        return ps;
    }

    /**
     * Finds all active patients that are ready for consultation. It is used by reporter.
     * @return List of Patients.
     */
    public List<Patient> getPatientsWaitingConsultation() {
        List<Patient> pw = entityManager.createQuery("from Patient p WHERE p.readyForConsultation = 1 and p.active = 1")
                .getResultList();

        return pw;
    }

    /**
     * This method is checking if a specific doctor consults a specific patient. If the query exists then return true, and this case means that
     * the doctor with id = did consults the patient with id = pid
     *
     * @param pid
     * @param did
     * @return true or false
     */
    public boolean patientAndDoctorConnection(Long pid, Long did) {
        List<Patient> match = entityManager.createQuery("from Patient p WHERE p.id = :pid and p.doctor.id = :did")
                .setParameter("pid", pid)
                .setParameter("did", did)
                .getResultList();

        return match.size() == 1;
    }
}
