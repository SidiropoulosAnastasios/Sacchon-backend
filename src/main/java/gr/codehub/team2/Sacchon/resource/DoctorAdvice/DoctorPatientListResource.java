package gr.codehub.team2.Sacchon.resource.DoctorAdvice;

import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.representation.PatientRepresentation;
import org.restlet.resource.Get;

import java.util.List;

public interface DoctorPatientListResource {

    @Get("json")
    public List<PatientRepresentation> getPatients() throws NotFoundException;
}
