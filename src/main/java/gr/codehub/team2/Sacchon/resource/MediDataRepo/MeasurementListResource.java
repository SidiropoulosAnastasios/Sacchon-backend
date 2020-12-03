package gr.codehub.team2.Sacchon.resource.MediDataRepo;

import gr.codehub.team2.Sacchon.exceptions.BadEntityException;
import gr.codehub.team2.Sacchon.exceptions.ForbiddenException;
import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.representation.MeasurementRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import java.util.List;

public interface MeasurementListResource {

    @Get("json")
    public List<MeasurementRepresentation> getMeasurements() throws NotFoundException, ForbiddenException;

    @Post("json")
    public MeasurementRepresentation addMeasurement(MeasurementRepresentation measurementIn) throws BadEntityException;
}
