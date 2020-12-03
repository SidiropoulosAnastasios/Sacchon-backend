package gr.codehub.team2.Sacchon.resource.MediDataRepo;

import gr.codehub.team2.Sacchon.exceptions.BadEntityException;
import gr.codehub.team2.Sacchon.exceptions.ForbiddenException;
import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.representation.MeasurementRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

public interface MeasurementResource {
    @Get("json")
    public MeasurementRepresentation getMeasurement() throws NotFoundException, ForbiddenException;

    @Delete
    public void removeMeasurement() throws NotFoundException, ForbiddenException;

    @Put("json")
    public MeasurementRepresentation updateMeasurement(MeasurementRepresentation measurementReprIn)
            throws NotFoundException, BadEntityException, ForbiddenException;

}
