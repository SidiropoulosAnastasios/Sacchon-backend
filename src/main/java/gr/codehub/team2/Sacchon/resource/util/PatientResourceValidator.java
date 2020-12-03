package gr.codehub.team2.Sacchon.resource.util;

import gr.codehub.team2.Sacchon.exceptions.BadEntityException;
import gr.codehub.team2.Sacchon.representation.MeasurementRepresentation;
import gr.codehub.team2.Sacchon.representation.PatientRepresentation;

public class PatientResourceValidator {
    /**
     * Checks that the given entity is not null.
     *
     * @param entity
     *            The entity to check.
     * @throws BadEntityException
     *             In case the entity is null.
     */
    public static void notNull(Object entity) throws BadEntityException {
        if (entity == null) {
            throw new BadEntityException("No input entity");
        }
    }

    /**
     * Checks that the given patient is valid.
     *
     * @param patientRepresentation
     * @throws BadEntityException
     * In case the data given are not valid.
     */
    public static void validatePatient(PatientRepresentation patientRepresentation)
            throws BadEntityException {
        if ( patientRepresentation.getName()==null) {
            throw new BadEntityException(
                    "Patient's name cannot be null");
        }
        if ( patientRepresentation.getDob()==null) {
            throw new BadEntityException(
                    "Patient's date of birth cannot be null");
        }
        if ( patientRepresentation.getGender()==null) {
            throw new BadEntityException(
                    "Patient's gender cannot be null");
        }
        if ( patientRepresentation.getDiabetesType()==null) {
            throw new BadEntityException(
                    "Patient's diabetes type cannot be null");
        }
        if ( patientRepresentation.getDateDiagnosed()==null) {
            throw new BadEntityException(
                    "Patient's date of first date diagnosed cannot be null");
        }
//        if ( patientRepresentation.getUsername()==null) {
//            throw new BadEntityException(
//                    "Patient's username cannot be null");
//        }
//        if ( patientRepresentation.getPassword()==null) {
//            throw new BadEntityException(
//                    "Patient's password cannot be null");
//        }
    }

    /**
     * Checks that the given measurement is valid.
     *
     * @param measurementRepresentation
     * @throws BadEntityException
     * In case the data given are not valid.
     */
    public static void validateMeasurement(MeasurementRepresentation measurementRepresentation)
            throws BadEntityException {
        if (measurementRepresentation.getDate() == null) {
            throw new BadEntityException("Measurement Date cannot be null");
        }
        if (measurementRepresentation.getGlucoseLevel() == 0) {
            throw new BadEntityException("Measurement glucose level cannot be 0");
        }
        if (measurementRepresentation.getCarbIntake() == 0) {
            throw new BadEntityException("Measurement Carb Intake cannot be 0");
        }
    }

}
