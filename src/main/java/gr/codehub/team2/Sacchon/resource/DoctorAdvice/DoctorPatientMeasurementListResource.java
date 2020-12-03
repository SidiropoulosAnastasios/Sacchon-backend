package gr.codehub.team2.Sacchon.resource.DoctorAdvice;

import gr.codehub.team2.Sacchon.exceptions.ForbiddenException;
import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.representation.MeasurementRepresentation;
import org.restlet.resource.Get;

import java.util.List;

public interface DoctorPatientMeasurementListResource {

    @Get("json")
    public List<MeasurementRepresentation> getMeasurements() throws NotFoundException, ForbiddenException;
}
