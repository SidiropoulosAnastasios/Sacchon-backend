package gr.codehub.team2.Sacchon.resource.MediDataRepo;

import gr.codehub.team2.Sacchon.exceptions.ForbiddenException;
import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.representation.MeasurementRepresentation;
import org.restlet.resource.Get;

public interface MeasurementAverageResource {

    @Get("json")
    public MeasurementRepresentation getMeasurementAverage() throws NotFoundException, ForbiddenException;

}

