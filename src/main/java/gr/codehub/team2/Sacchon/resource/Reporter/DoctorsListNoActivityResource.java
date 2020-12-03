package gr.codehub.team2.Sacchon.resource.Reporter;

import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.representation.DoctorRepresentation;
import org.restlet.resource.Get;

import java.util.List;

public interface DoctorsListNoActivityResource {
    @Get("json")
    public List<DoctorRepresentation> getDoctorsNoActivity() throws NotFoundException;
}
