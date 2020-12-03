package gr.codehub.team2.Sacchon.resource.DoctorAdvice;

import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.representation.DoctorRepresentation;

import org.restlet.resource.Get;
import org.restlet.resource.Put;

public interface DoctorResource {

    @Get("json")
    public DoctorRepresentation getDoctor() throws NotFoundException;

//    @Delete
    @Put("json")
    public void removeDoctor() throws NotFoundException;

//    @Put("json")
//    public DoctorRepresentation updateDoctor(DoctorRepresentation doctorRepresentationIn)
//            throws NotFoundException, BadEntityException;

}
