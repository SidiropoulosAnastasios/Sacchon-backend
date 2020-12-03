package gr.codehub.team2.Sacchon.resource.Reporter;

import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.representation.ConsultationRepresentation;
import org.restlet.resource.Get;

import java.util.List;

public interface ConsultationsOfDoctorOverTimeRangeResource {
    @Get("json")
    public List<ConsultationRepresentation> getConsultationsOfDoctorOverTimeRange() throws NotFoundException;
}
