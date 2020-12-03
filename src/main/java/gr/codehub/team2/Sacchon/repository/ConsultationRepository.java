package gr.codehub.team2.Sacchon.repository;

import gr.codehub.team2.Sacchon.model.Consultation;
import gr.codehub.team2.Sacchon.repository.lib.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class ConsultationRepository extends Repository<Consultation, Long> {

    private EntityManager entityManager;

    public ConsultationRepository(EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public Class<Consultation> getEntityClass() {
        return Consultation.class;
    }

    @Override
    public String getEntityClassName() {
        return Consultation.class.getName();
    }

    /**
     * Finds a List of consultations of a Doctor using his Doctor ID.
     * @param did
     * @return A list of his consultations.
     */
    public List<Consultation> findConsultations(Long did) {
        List<Consultation> cs = entityManager.createQuery("from Consultation c WHERE c.doctor.id = :did ORDER BY c.date DESC")
                .setParameter("did", did)
//                .setParameter("doctor", doctor)
                .getResultList();
        return cs;
    }

    /**
     * Finds a List of consultations of a Patient that is still active using his Patient ID.
     * @param pid
     * @return A list of Patient's consultations in descending date order.
     */
    public List<Consultation> findConsultationsByPatient(Long pid) {
        List<Consultation> cs = entityManager.createQuery("SELECT DISTINCT c from Consultation c INNER JOIN Patient p ON c.patient.id = :pid WHERE p.active = 1 ORDER BY c.date DESC")
                .setParameter("pid", pid)
                .getResultList();

        return cs;
    }

    /**
     * Finds a List of consultations of a Doctor using his Doctor ID for a specific date range.
     * @param startDate
     * @param endDate
     * @param did
     * @return A list of Doctor's consultations in descending date order.
     */
    public List<Consultation> findConsultationsOfDoctorOverTimeRange(LocalDate startDate, LocalDate endDate, Long did) {
        List<Consultation> cs = entityManager.createQuery("from Consultation c WHERE c.doctor.id = :did AND c.date >= :startDate AND c.date <= :endDate ORDER BY c.date DESC")
                .setParameter("did", did)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();

        return cs;
    }

    /**
     * Finds the latest consultation date of a Patient using Patient ID.
     * @param pid
     * @return The last date had consultation.
     */
    public LocalDate findLatestConsultationDateByPatient(Long pid) {
        LocalDate latestDate = (LocalDate) entityManager.createQuery("SELECT MAX(c.date) from Consultation c WHERE c.patient.id = :pid")
                .setParameter("pid", pid)
                .getSingleResult();

        return latestDate;
    }

}