package gr.codehub.team2.Sacchon.representation;

import gr.codehub.team2.Sacchon.model.ChiefDoctor;
import gr.codehub.team2.Sacchon.security.CustomRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChiefDoctorRepresentation {
    private String name;
    private String username;
    /**
     * The URL of this resource.
     */
    private String uri;

    /**
     *  This method is used in order to match Chief Doctor JSON Representation to Chief Doctor data.
     * @param chiefDoctorRepresentation
     * @return
     */
    static public ChiefDoctor getChiefDoctor(ChiefDoctorRepresentation chiefDoctorRepresentation) {
        ChiefDoctor chiefDoctor = new ChiefDoctor();
        chiefDoctor.setName(chiefDoctorRepresentation.getName());
        return chiefDoctor;
    }

    /** This method is used in order to represent Chief Doctor in JSON format.
     *
     * @param chiefDoctor
     * @return JSON Representation of Chief Doctor data.
     */
    static public ChiefDoctorRepresentation getChiefDoctorRepresentation(ChiefDoctor chiefDoctor){
        ChiefDoctorRepresentation chiefDoctorRepresentation = new ChiefDoctorRepresentation();
        chiefDoctorRepresentation.setName(chiefDoctor.getName());
        chiefDoctorRepresentation.setUri("http://localhost:9000/sacchon/reporter/");
        return chiefDoctorRepresentation;
    }
}
