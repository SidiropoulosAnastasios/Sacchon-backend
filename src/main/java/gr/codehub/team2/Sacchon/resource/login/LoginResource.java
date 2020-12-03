package gr.codehub.team2.Sacchon.resource.login;

import gr.codehub.team2.Sacchon.exceptions.BadEntityException;
import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.representation.ApplicationUserRepresentation;
import org.restlet.resource.Post;

public interface LoginResource {

    @Post("json")
    public ApplicationUserRepresentation loginUser(ApplicationUserRepresentation applicationUserRepresentation) throws NotFoundException, BadEntityException;
}

