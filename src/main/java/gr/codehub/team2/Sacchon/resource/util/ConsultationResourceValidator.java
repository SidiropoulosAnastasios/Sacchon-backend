package gr.codehub.team2.Sacchon.resource.util;

import gr.codehub.team2.Sacchon.exceptions.BadEntityException;
import gr.codehub.team2.Sacchon.representation.ConsultationRepresentation;

public class ConsultationResourceValidator {
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
     * @param consultationRepresentation
     * @throws BadEntityException
     */
    public static void validate(ConsultationRepresentation consultationRepresentation)
            throws BadEntityException {
        if (consultationRepresentation.getMedication()==null) {
            throw new BadEntityException(
                    "Consultation medication cannot be null");
        }
        if (consultationRepresentation.getDosage()==null) {
            throw new BadEntityException(
                    "Consultation dosage cannot be null");
        }
    }
}
