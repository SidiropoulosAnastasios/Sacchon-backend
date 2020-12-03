package gr.codehub.team2.Sacchon.resource.MediDataRepo;

import gr.codehub.team2.Sacchon.exceptions.ForbiddenException;
import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.representation.ConsultationRepresentation;
import org.restlet.resource.Get;

public interface PatientConsultationResource {

    @Get("json")
    public ConsultationRepresentation getConsultation() throws NotFoundException, ForbiddenException;

}
