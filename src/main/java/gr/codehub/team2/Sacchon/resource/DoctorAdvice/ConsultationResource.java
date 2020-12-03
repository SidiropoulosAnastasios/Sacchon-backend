package gr.codehub.team2.Sacchon.resource.DoctorAdvice;

import gr.codehub.team2.Sacchon.exceptions.BadEntityException;
import gr.codehub.team2.Sacchon.exceptions.ForbiddenException;
import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.representation.ConsultationRepresentation;

import org.restlet.resource.Get;
import org.restlet.resource.Put;

public interface ConsultationResource {

    @Get("json")
    public ConsultationRepresentation getConsultation() throws NotFoundException, ForbiddenException;


    @Put("json")
    public ConsultationRepresentation updateConsultation(ConsultationRepresentation consultationReprIn)
            throws NotFoundException, BadEntityException, ForbiddenException;

}
