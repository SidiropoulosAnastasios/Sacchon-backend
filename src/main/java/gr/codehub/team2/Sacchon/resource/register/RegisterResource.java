package gr.codehub.team2.Sacchon.resource.register;

import gr.codehub.team2.Sacchon.exceptions.BadEntityException;
import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.representation.ApplicationUserRepresentation;
import gr.codehub.team2.Sacchon.representation.UserPatientRepresentation;
import org.restlet.resource.Post;

public interface RegisterResource {

    @Post("json")
    public ApplicationUserRepresentation registerUserPatient(UserPatientRepresentation userPatientRepresentation) throws NotFoundException, BadEntityException;
}
