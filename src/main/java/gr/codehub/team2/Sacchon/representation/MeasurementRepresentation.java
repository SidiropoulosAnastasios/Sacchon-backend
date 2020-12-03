package gr.codehub.team2.Sacchon.representation;

import gr.codehub.team2.Sacchon.model.Measurement;

import gr.codehub.team2.Sacchon.model.Patient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Optional;

@Setter
@Getter
@NoArgsConstructor
public class MeasurementRepresentation {
    private String date;
    private double glucoseLevel;
    private double carbIntake;
    private Long patientId;
    private Long id;
    /**
     * The URL of this resource.
     */
    private String uri;

    /** This method is used in order to to match Consultation JSON Representation to Consultation data using data from Patient.
     *
     * @param measurementRepresentation
     * @param patient
     * @return Measurement data.
     */
    static public Measurement getMeasurement(MeasurementRepresentation measurementRepresentation, Optional<Patient> patient) {
        Measurement measurement = new Measurement();
        measurement.setDate(LocalDate.parse(measurementRepresentation.getDate()));
        measurement.setGlucoseLevel(measurementRepresentation.getGlucoseLevel());
        measurement.setCarbIntake(measurementRepresentation.getCarbIntake());
        measurement.setPatient(patient.get());
        return measurement;
    }

    /** This method is used in order to represent Measurement in JSON format.
     *
     * @param measurement
     * @return JSON Representation of Measurement data.
     */
    static public MeasurementRepresentation getMeasurementRepresentation(Measurement measurement) {
        MeasurementRepresentation measurementRepresentation = new MeasurementRepresentation();
        measurementRepresentation.setId(measurement.getId());
        measurementRepresentation.setDate(measurement.getDate().toString());
        measurementRepresentation.setGlucoseLevel(measurement.getGlucoseLevel());
        measurementRepresentation.setCarbIntake(measurement.getCarbIntake());
        measurementRepresentation.setPatientId(measurement.getPatient().getId());
        measurementRepresentation.setUri("http://localhost:9000/sacchon/patients/" + measurement.getPatient().getId() + "/measurements/" + measurement.getId());
        return measurementRepresentation;
    }
}
