package gr.codehub.team2.Sacchon.repository;

import gr.codehub.team2.Sacchon.repository.lib.Repository;
import gr.codehub.team2.Sacchon.security.dao.ApplicationUser;
import org.restlet.Request;
import org.restlet.security.User;

import javax.persistence.EntityManager;
import java.util.Optional;

public class ApplicationUserRepository extends Repository<ApplicationUser, Long> {

    private EntityManager entityManager;

    public ApplicationUserRepository(EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public Class getEntityClass() {
        return ApplicationUser.class;
    }

    @Override
    public String getEntityClassName() {
        return ApplicationUser.class.getName();
    }

    /**
     * Finds applicationUser by his username and password. Needed for the Login Resource Impl
     * @param username
     * @param password
     * @return Optional of applicationUser if found.
     */
    public Optional<ApplicationUser> findUserLogin(String username, String password) {
        ApplicationUser applicationUser = (ApplicationUser) entityManager.createQuery("from ApplicationUser a WHERE a.username =: username AND a.password =: password AND a.active = 1")
                .setParameter("username", username)
                .setParameter("password", password)
                .getSingleResult();
        return applicationUser != null ? Optional.of(applicationUser) : Optional.empty();
    }

    /**
     * Finds an applicationUser by his Patient ID. Needed for the remove method for patient.
     * @param pid
     * @return Optional of applicationUser if found.
     */
    public Optional<ApplicationUser> findPatientUser(Long pid) {
        ApplicationUser applicationUser = (ApplicationUser) entityManager.createQuery("from ApplicationUser a WHERE a.patient.id = :pid")
                .setParameter("pid", pid)
                .getSingleResult();
        return applicationUser != null ? Optional.of(applicationUser) : Optional.empty();
    }

    /**
     * Finds an applicationUser by his Doctor ID. Needed for the remove method for doctor.
     * @param did
     * @return Optional of applicationUser if found.
     */
    public Optional<ApplicationUser> findDoctorUser(Long did) {
        ApplicationUser applicationUser = (ApplicationUser) entityManager.createQuery("from ApplicationUser a WHERE a.doctor.id = :did")
                .setParameter("did", did)
                .getResultList();
        return applicationUser != null ? Optional.of(applicationUser) : Optional.empty();
    }

    /**
     * This method finds the id of the user during the login. That's necessary for accessing the correct and permitted rest routes!
     *
     * @return
     */
    public Long findUserId() {
        Request request = Request.getCurrent();
        User user = request.getClientInfo().getUser();
        String userName = user.getName();
        Long did = (Long) entityManager.createQuery("SELECT a.doctorId from ApplicationUser a WHERE a.username = :userName")
                .setParameter("userName", userName)
                .getSingleResult();

        Long pid = (Long) entityManager.createQuery("SELECT a.patient.id from ApplicationUser a WHERE a.username = :userName")
                .setParameter("userName", userName)
                .getSingleResult();


        Long cid = (Long) entityManager.createQuery("SELECT a.chiefDoctor.id from ApplicationUser a WHERE a.username = :userName")
                .setParameter("userName", userName)
                .getSingleResult();

        if (did > 0) {
            return did;
        } else if (pid > 0) {
            return pid;
        } else if (cid > 0) {
            return cid;
        }
        return null;
    }
}
