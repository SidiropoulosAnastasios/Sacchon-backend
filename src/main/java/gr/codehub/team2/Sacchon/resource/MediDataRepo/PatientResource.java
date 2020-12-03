package gr.codehub.team2.Sacchon.resource.MediDataRepo;

import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.representation.PatientRepresentation;

import org.restlet.resource.Get;
import org.restlet.resource.Put;

public interface PatientResource {

    @Get("json")
    public PatientRepresentation getPatient() throws NotFoundException;

    @Put("json")
    public void removePatient() throws NotFoundException;

//    @Put("json")
//    public PatientRepresentation updatePatient(PatientRepresentation patientRepresentationIn)
//            throws NotFoundException, BadEntityException;

}
