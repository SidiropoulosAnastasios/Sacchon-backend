package gr.codehub.team2.Sacchon.representation;

import gr.codehub.team2.Sacchon.exceptions.NotFoundException;
import gr.codehub.team2.Sacchon.security.CustomRole;
import gr.codehub.team2.Sacchon.security.dao.ApplicationUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ApplicationUserRepresentation {
    private String username;
    private String password;
    private int role;
    private Long id;

    /** This method is used in order to match Application User JSON Representation to an Application User data.
     *
     * @param applicationUserRepresentation
     * @return Application User data.
     */
    static public ApplicationUser getApplicationUser(ApplicationUserRepresentation applicationUserRepresentation) {
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setUsername(applicationUserRepresentation.getUsername());
        applicationUser.setPassword(applicationUserRepresentation.getPassword());
        applicationUser.setRole(CustomRole.getRoleValueNumber(applicationUserRepresentation.getRole()));
        return applicationUser;
    }

    /** This method is used in order to represent Application User in JSON format.
     *
     * @param applicationUser
     * @return JSON Representation of Application User data.
     * @throws NotFoundException
     */
    static public ApplicationUserRepresentation getApplicationUserRepresentation(ApplicationUser applicationUser) throws NotFoundException {
        ApplicationUserRepresentation applicationUserRepresentation = new ApplicationUserRepresentation();
        applicationUserRepresentation.setUsername(applicationUser.getUsername());
        applicationUserRepresentation.setPassword(applicationUser.getPassword());
        applicationUserRepresentation.setRole(applicationUser.getRole().getRoleNumber());
        if (applicationUser.getRole().getRoleNumber() == CustomRole.ROLE_CHIEF_DOCTOR.getRoleNumber()) {
            applicationUserRepresentation.setId(applicationUser.getChiefDoctor().getId());
        } else if (applicationUser.getRole().getRoleNumber() == CustomRole.ROLE_PATIENT.getRoleNumber()) {
            applicationUserRepresentation.setId((applicationUser.getPatient().getId()));
        } else if (applicationUser.getRole().getRoleNumber() == CustomRole.ROLE_DOCTOR.getRoleNumber()) {
            applicationUserRepresentation.setId((applicationUser.getDoctor().getId()));
        } else {
            throw new NotFoundException("Can't find correct credentials");
        }
        return applicationUserRepresentation;
    }
}
