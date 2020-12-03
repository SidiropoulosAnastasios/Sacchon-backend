package gr.codehub.team2.Sacchon.resource.util;

import gr.codehub.team2.Sacchon.exceptions.BadEntityException;
import gr.codehub.team2.Sacchon.representation.MeasurementRepresentation;

public class MeasurementResourceValidator {
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
     * Checks that the given entity is valid.
     *
     * @param measurementRepresentation
     * @throws BadEntityException
     */
    public static void validate(MeasurementRepresentation measurementRepresentation)
            throws BadEntityException {
        if ( measurementRepresentation.getGlucoseLevel()== 0) {
            throw new BadEntityException("Measurement_glukoselevel cannot be null");
        }
        if ( measurementRepresentation.getCarbIntake()== 0) {
            throw new BadEntityException("Measurement_CarbInt cannot be null");
        }
    }
}
