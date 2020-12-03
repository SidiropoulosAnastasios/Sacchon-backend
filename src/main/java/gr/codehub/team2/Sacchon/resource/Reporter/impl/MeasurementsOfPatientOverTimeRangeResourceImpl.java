package gr.codehub.team2.Sacchon.resource.Reporter.impl;

import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.model.Measurement;
import gr.codehub.team2.Sacchon.repository.MeasurementRepository;
import gr.codehub.team2.Sacchon.repository.util.JpaUtil;
import gr.codehub.team2.Sacchon.representation.MeasurementRepresentation;
import gr.codehub.team2.Sacchon.resource.Reporter.MeasurementsOfPatientOverTimeRangeResource;
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

public class MeasurementsOfPatientOverTimeRangeResourceImpl extends ServerResource implements MeasurementsOfPatientOverTimeRangeResource {

    static final Logger LOGGER = Engine.getLogger(MeasurementsOfPatientOverTimeRangeResourceImpl.class);
    private MeasurementRepository measurementRepository;
    private EntityManager em;
    private long pid;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Initialization of measurement repository, parsing Patient id and dates from front end.
     */
    @Override
    protected void doInit() {
        LOGGER.info("Initialising MeasurementsOfPatientOverTimeRange resource starts");
        try {
            em = JpaUtil.getEntityManager();
            measurementRepository = new MeasurementRepository(em);
            pid = Long.parseLong(getAttribute("patient_id"));
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

    /** This method is used by Chief Doctors in order to get Measurements of a Patient for a specific Date range.
     *
     * @return List of Measurements Representation in JSON Format.
     * @throws NotFoundException
     */
    @Override
    public List<MeasurementRepresentation> getMeasurementsOfPatientOverTimeRange() throws NotFoundException {
        LOGGER.finer("Get measurements of patient over a time range");
        ResourceUtils.checkRole(this, CustomRole.ROLE_CHIEF_DOCTOR.getRoleName());
        LOGGER.finer("Chief doctor allowed to see the measurements of patient over a time range");

        try {
            List<Measurement> measurements = measurementRepository.findMeasurementsOfPatientOverTimeRange(startDate, endDate, pid);
            List<MeasurementRepresentation> measurementRepresentationList = new ArrayList<>();
            measurements.forEach(measurement -> measurementRepresentationList.add(MeasurementRepresentation.getMeasurementRepresentation(measurement)));
            LOGGER.finer("Patients with no activity found");
            return measurementRepresentationList;
        }
        catch (Exception e){
            LOGGER.finer("Patients with no activity not found");
            throw new NotFoundException("Patients with no activity not found");
        }
    }

}
