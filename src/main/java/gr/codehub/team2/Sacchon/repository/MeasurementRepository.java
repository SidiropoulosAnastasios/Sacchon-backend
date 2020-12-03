package gr.codehub.team2.Sacchon.repository;

import gr.codehub.team2.Sacchon.model.Measurement;
import gr.codehub.team2.Sacchon.repository.lib.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class MeasurementRepository extends Repository<Measurement, Long> {
    private EntityManager entityManager;

    public MeasurementRepository(EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public Class<Measurement> getEntityClass() {
        return Measurement.class;
    }

    @Override
    public String getEntityClassName() {
        return Measurement.class.getName();
    }

    /**
     * Finds measurements List of a Patient by his Patient ID.
     * @param pid
     * @return List of measurements in descending date order.
     */
    public List<Measurement> findMyMeasurements(Long pid) {
        List<Measurement> ms = entityManager.createQuery("SELECT m from Measurement m WHERE m.patient.id = :pid ORDER BY m.date DESC")
                .setParameter("pid", pid)
                .getResultList();
        return ms;
    }

    /**
     * Finds Measurements of Patient by his Patient ID if Patient is active.
     * @param pid
     * @return List of measurements in descending date order.
     */
    public List<Measurement> findMeasurementsOfPatient(Long pid) {
        List<Measurement> ms = entityManager.createQuery("SELECT m from Measurement m INNER JOIN Patient p ON m.patient.id = :pid WHERE p.active = 1 ORDER BY m.date DESC")
                .setParameter("pid", pid)
                .getResultList();
        return ms;
    }

    /**
     * Finds Average Carb Intake for a Specific Patient using his Patient ID in a specific date range.
     * @param startDate
     * @param endDate
     * @param pid
     * @return Average Carb Intake
     */
    public Double findAverageCarbIntake(LocalDate startDate, LocalDate endDate, Long pid) {
        Double avg = (Double) entityManager.createQuery("SELECT AVG(m.carbIntake) from Measurement m WHERE m.patient.id = :pid AND m.date >= :startDate AND m.date <= :endDate")
                .setParameter("pid", pid)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getSingleResult();
        return avg;
    }

    /**
     * Finds Daily Average Blood Glucose level for a Specific Patient using his Patient ID in a specific date range.
     * @param startDate
     * @param endDate
     * @param pid
     * @return List of daily Average Blood Glucose levels.
     */
    public List<Double> findAverageDailyBloodGlucose(LocalDate startDate, LocalDate endDate, Long pid) {
        List<Double> avg = entityManager.createQuery("SELECT AVG(m.glucoseLevel) from Measurement m WHERE m.patient.id = :pid AND m.date >= :startDate AND m.date <= :endDate GROUP BY m.date")
                .setParameter("pid", pid)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
        return avg;
    }

    /**
     * Finds measurements data for a Specific Patient using his Patient ID in a specific date range.
     * @param startDate
     * @param endDate
     * @param pid
     * @return List of measurements in descending date order.
     */
    public List<Measurement> findMeasurementsOfPatientOverTimeRange(LocalDate startDate, LocalDate endDate, Long pid) {
        List<Measurement> ci = entityManager.createQuery("from Measurement m WHERE m.patient.id = :pid AND (m.date >= :startDate) AND (m.date <= :endDate) ORDER BY m.date DESC")
                .setParameter("pid", pid)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
        return ci;
    }

    /**
     * Finds the first Date of a measurement for a Patient using his Patient ID. It is needed in order to find a new Patient that has no consultation yet.
     * @param pid
     * @return Date of first measurement.
     */
    public LocalDate findFirstDateOfPatientsMeasurements(Long pid) {
        LocalDate minDate = (LocalDate) entityManager.createQuery("SELECT MIN(m.date) from Measurement m WHERE m.patient.id = :pid")
                .setParameter("pid", pid)
                .getSingleResult();
        return minDate;
    }
}