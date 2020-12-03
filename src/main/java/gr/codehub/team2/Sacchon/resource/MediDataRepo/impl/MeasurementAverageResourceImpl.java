package gr.codehub.team2.Sacchon.resource.MediDataRepo.impl;

import gr.codehub.team2.Sacchon.exceptions.ForbiddenException;
import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.model.Patient;
import gr.codehub.team2.Sacchon.repository.MeasurementRepository;
import gr.codehub.team2.Sacchon.repository.PatientRepository;
import gr.codehub.team2.Sacchon.repository.util.JpaUtil;
import gr.codehub.team2.Sacchon.representation.MeasurementRepresentation;
import gr.codehub.team2.Sacchon.resource.MediDataRepo.MeasurementAverageResource;
import gr.codehub.team2.Sacchon.resource.util.ResourceUtils;
import gr.codehub.team2.Sacchon.security.CustomRole;
import org.restlet.engine.Engine;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.logging.Logger;

public class MeasurementAverageResourceImpl extends ServerResource implements MeasurementAverageResource {
    public static final Logger LOGGER = Engine.getLogger(MeasurementAverageResourceImpl.class);
    private PatientRepository patientRepository;
    private MeasurementRepository measurementRepository;

    private EntityManager em;
    private long pid;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Initialization of repositories and parsing ids from front end.
     */
    @Override
    protected void doInit() {
        LOGGER.info("Initialising measurement resource starts");
        try {
            em = JpaUtil.getEntityManager();
            patientRepository = new PatientRepository(em);
            measurementRepository = new MeasurementRepository(em);
            pid = Long.parseLong(getAttribute("patient_id"));
            startDate = LocalDate.parse(getAttribute("start_date"));
            endDate = LocalDate.parse(getAttribute("end_date"));
        }
        catch(Exception ex){

            throw new ResourceException(ex);
        }
        LOGGER.info("Initialising measurement resource ends");

    }
    /**
     * Release method that closes entity manager.
     */
    @Override
    protected void doRelease() {
        em.close();
    }

    /** This method is used from Patients to find their Average measurements.
     *
     * @return Average Glucose Level and Carb Intake Representation in JSON Format.
     * @throws NotFoundException
     * @throws ForbiddenException
     */
    @Override
    public MeasurementRepresentation getMeasurementAverage() throws NotFoundException, ForbiddenException {
        LOGGER.finer("Trying to get average measurements");
        ResourceUtils.checkRole(this, CustomRole.ROLE_PATIENT.getRoleName());
        LOGGER.finer("Patient allowed to get average measurements");
        Optional<Patient> patient = patientRepository.findById(pid);
        setExisting(patient.isPresent());
        if (!patient.isPresent())  throw new NotFoundException("Patient is not found");
        LOGGER.finer("Patient allowed to get measurements average");
        List<Double> glucoseDailyAverage = measurementRepository.findAverageDailyBloodGlucose(startDate,endDate,pid);
        Double carbAverage = measurementRepository.findAverageCarbIntake(startDate, endDate, pid);
        LOGGER.finer("Average measurements are available");
        MeasurementRepresentation measurementRepresentation = new MeasurementRepresentation();
        OptionalDouble average = glucoseDailyAverage
                .stream()
                .mapToDouble(a -> a)
                .average();
        measurementRepresentation.setGlucoseLevel(average.getAsDouble());
        measurementRepresentation.setCarbIntake(carbAverage);
        measurementRepresentation.setId(patient.get().getId());
        return measurementRepresentation;
    }


}
