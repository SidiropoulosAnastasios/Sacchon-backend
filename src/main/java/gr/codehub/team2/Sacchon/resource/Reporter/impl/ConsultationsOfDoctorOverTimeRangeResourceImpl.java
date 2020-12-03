package gr.codehub.team2.Sacchon.resource.Reporter.impl;

import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.model.Consultation;
import gr.codehub.team2.Sacchon.repository.ConsultationRepository;
import gr.codehub.team2.Sacchon.repository.util.JpaUtil;
import gr.codehub.team2.Sacchon.representation.ConsultationRepresentation;
import gr.codehub.team2.Sacchon.resource.Reporter.ConsultationsOfDoctorOverTimeRangeResource;
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

public class ConsultationsOfDoctorOverTimeRangeResourceImpl extends ServerResource implements ConsultationsOfDoctorOverTimeRangeResource {

    public static final Logger LOGGER = Engine.getLogger(ConsultationsOfDoctorOverTimeRangeResourceImpl.class);
    private ConsultationRepository consultationRepository;
    private EntityManager em;
    private long did;
    private LocalDate startDate;
    private LocalDate endDate;
    /**
     * Initialization of repositories, parsing Doctor id and dates from front end.
     */
    @Override
    protected void doInit() {
        LOGGER.info("Initialising MeasurementsOfPatientOverTimeRange resource starts");
        try {
            em = JpaUtil.getEntityManager();
            consultationRepository = new ConsultationRepository(em);
            did = Long.parseLong(getAttribute("doctor_id"));
            startDate = LocalDate.parse(getAttribute("start_date"));
            endDate = LocalDate.parse(getAttribute("end_date"));

        } catch (Exception e) {
            throw new ResourceException(e);
        }
        LOGGER.info("Initialising DoctorListNoActivity resource has ended");
    }
    /**
     * Release method that closes entity manager.
     */
    @Override
    protected void doRelease() {
        em.close();
    }

    /** This method is used by Chief Doctors in order to get Consultations of Doctors for a specific date range.
     *
     * @return List of Consultations Representation in JSON Format.
     * @throws NotFoundException
     */
    @Override
    public List<ConsultationRepresentation> getConsultationsOfDoctorOverTimeRange() throws NotFoundException {
        LOGGER.finer("Get consultations of doctor over a time range");
        ResourceUtils.checkRole(this, CustomRole.ROLE_CHIEF_DOCTOR.getRoleName());
        LOGGER.finer("Chief doctor allowed to see the consultations of doctor over a time range");

        try {
            List<Consultation> consultations = consultationRepository.findConsultationsOfDoctorOverTimeRange(startDate, endDate, did);
            List<ConsultationRepresentation> consultationRepresentationList = new ArrayList<>();
            consultations.forEach(consultation -> consultationRepresentationList.add(ConsultationRepresentation.getConsultationRepresentation(consultation)));
            LOGGER.finer("Consultations of doctor found");
            return consultationRepresentationList;
        }
        catch (Exception e){
            LOGGER.finer("Consultations of doctor not found");
            throw new NotFoundException("Consultations of doctor not found");
        }
    }
}
