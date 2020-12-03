package gr.codehub.team2.Sacchon.resource.MediDataRepo;

import gr.codehub.team2.Sacchon.exceptions.BadEntityException;
import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.representation.PatientRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;

import java.util.List;

public interface PatientListResource {
    @Get("json")
    public List<PatientRepresentation> getPatients() throws NotFoundException;

}
