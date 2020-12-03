package gr.codehub.team2.Sacchon.resource.register;

import gr.codehub.team2.Sacchon.exceptions.BadEntityException;
import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.representation.ApplicationUserRepresentation;
import gr.codehub.team2.Sacchon.representation.UserDoctorRepresentation;
import org.restlet.resource.Post;

public interface RegisterDoctorResource {

    @Post("json")
    public ApplicationUserRepresentation registerUserDoctor(UserDoctorRepresentation userDoctorRepresentation) throws NotFoundException, BadEntityException;
}
