package gr.codehub.team2.Sacchon.resource.Reporter;

import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.representation.*;
import org.restlet.resource.Get;

public interface ChiefDoctorResource {

    @Get("json")
    public ChiefDoctorRepresentation getChiefDoctor() throws NotFoundException;

//    @Post("json")
//    public ChiefDoctorRepresentation addChiefDoctor(ChiefDoctorRepresentation chiefDoctorIn) throws BadEntityException;

//    @Delete
//    public void removeChiefDoctor() throws NotFoundException;

//    @Put("json")
//    public ChiefDoctorRepresentation updateChiefDoctor(ChiefDoctorRepresentation ChiefDoctorRepresentationIn)
//            throws NotFoundException, BadEntityException;

}
