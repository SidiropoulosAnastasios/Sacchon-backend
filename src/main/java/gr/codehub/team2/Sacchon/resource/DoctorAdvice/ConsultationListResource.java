package gr.codehub.team2.Sacchon.resource.DoctorAdvice;

import gr.codehub.team2.Sacchon.exceptions.BadEntityException;
import gr.codehub.team2.Sacchon.exceptions.ForbiddenException;
import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.representation.ConsultationRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import java.util.List;

public interface ConsultationListResource {

    @Get("json")
    public List<ConsultationRepresentation> getConsultations() throws NotFoundException, ForbiddenException;

    @Post("json")
    public ConsultationRepresentation addConsultation(ConsultationRepresentation consultationIn) throws BadEntityException;

}
