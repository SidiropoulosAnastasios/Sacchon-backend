package gr.codehub.team2.Sacchon.repository;

import gr.codehub.team2.Sacchon.model.Doctor;
import gr.codehub.team2.Sacchon.repository.lib.Repository;
import org.restlet.Request;
import org.restlet.security.User;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class DoctorRepository extends Repository<Doctor, Long> {

    private EntityManager entityManager;

    public DoctorRepository(EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public Class<Doctor> getEntityClass() {
        return Doctor.class;
    }

    @Override
    public String getEntityClassName() {
        return Doctor.class.getName();
    }

    /**
     * Finds a list of Doctors that serve a consultation in a specific date range.
     * @param startDate
     * @param endDate
     * @return List of Doctors.
     */
    public List<Doctor> findDoctorsActivity(LocalDate startDate, LocalDate endDate) {
        List<Doctor> ds = entityManager.createQuery("SELECT DISTINCT d from Doctor d INNER JOIN Consultation c ON c.doctor.id = d.id WHERE c.date >= :startDate AND c.date <= :endDate")
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();

        return ds;
    }

    /**
     * Finds current Doctor by his Doctor ID.
     * @return Doctor ID
     */
    public Long findUserDoctorId() {
        Request request = Request.getCurrent();
        User user = request.getClientInfo().getUser();
        String userName = user.getName();
        Long did = (Long) entityManager.createQuery("SELECT a.doctor.id from ApplicationUser a WHERE a.username = :userName")
                .setParameter("userName", userName)
                .getSingleResult();
        return did;
    }

}
