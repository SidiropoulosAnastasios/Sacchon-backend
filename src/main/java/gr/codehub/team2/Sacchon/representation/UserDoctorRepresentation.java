package gr.codehub.team2.Sacchon.representation;

import gr.codehub.team2.Sacchon.model.Doctor;
import gr.codehub.team2.Sacchon.security.CustomRole;
import gr.codehub.team2.Sacchon.security.dao.ApplicationUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDoctorRepresentation {
    private ApplicationUserRepresentation applicationUserRepresentation;
    private DoctorRepresentation doctorRepresentation;
    private String username; //must be unique
    private String password;
    private String name;
    private String doctorPermissionCode;

    /** This method is used in order to match Doctor JSON Representation to Doctor data at register process.
     *
     * @param userDoctorRepresentation
     * @return Doctor Data.
     */
    static public Doctor getUserDoctor(UserDoctorRepresentation userDoctorRepresentation) {
        Doctor doctor = new Doctor();
        doctor.setName(userDoctorRepresentation.getName());
        doctor.setDoctorPermissionCode(userDoctorRepresentation.getDoctorPermissionCode());
        return doctor;
    }

    /** This method is used in order to represent Doctor Application User in JSON format at register process.
     *
     * @param userDoctorRepresentation
     * @return JSON Representation of Doctor Application User data.
     */
    static public ApplicationUser getUser(UserDoctorRepresentation userDoctorRepresentation) {
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setUsername(userDoctorRepresentation.getUsername());
        applicationUser.setPassword(userDoctorRepresentation.getPassword());
        applicationUser.setRole(CustomRole.getRoleValueNumber(CustomRole.ROLE_DOCTOR.getRoleNumber()));
        return applicationUser;
    }
}
