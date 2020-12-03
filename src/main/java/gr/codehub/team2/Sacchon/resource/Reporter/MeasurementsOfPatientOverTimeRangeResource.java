package gr.codehub.team2.Sacchon.resource.Reporter;

import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.representation.MeasurementRepresentation;
import org.restlet.resource.Get;

import java.util.List;

public interface MeasurementsOfPatientOverTimeRangeResource {
    @Get("json")
    public List<MeasurementRepresentation> getMeasurementsOfPatientOverTimeRange() throws NotFoundException;
}
