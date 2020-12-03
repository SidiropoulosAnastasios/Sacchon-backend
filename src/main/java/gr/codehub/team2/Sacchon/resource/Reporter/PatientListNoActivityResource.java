package gr.codehub.team2.Sacchon.resource.Reporter;

import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.representation.PatientRepresentation;
import org.restlet.resource.Get;

import java.util.List;

public interface PatientListNoActivityResource {
    @Get("json")
    public List<PatientRepresentation> getPatientsNoActivity() throws NotFoundException;
}
