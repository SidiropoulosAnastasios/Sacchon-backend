package gr.codehub.team2.Sacchon.representation;

import gr.codehub.team2.Sacchon.model.Patient;
import gr.codehub.team2.Sacchon.security.CustomRole;
import gr.codehub.team2.Sacchon.security.dao.ApplicationUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UserPatientRepresentation {
    private ApplicationUserRepresentation applicationUserRepresentation;
    private PatientRepresentation patientRepresentation;
    private String username; //must be unique
    private String password;
    private String name;
    private String dob;
    private String gender;
    private String diabetesType;
    private String dateDiagnosed;

    /** This method is used in order to match Patient JSON Representation to Patient data at register process.
     *
     * @param userPatientRepresentation
     * @return Patient Data.
     */
    static public Patient getUserPatient(UserPatientRepresentation userPatientRepresentation) {
        Patient patient = new Patient();
        patient.setName(userPatientRepresentation.getName());
        patient.setDob(LocalDate.parse(userPatientRepresentation.getDob()));
        patient.setGender(userPatientRepresentation.getGender());
        patient.setDiabetesType(userPatientRepresentation.getDiabetesType());
        patient.setDateDiagnosed(LocalDate.parse(userPatientRepresentation.getDateDiagnosed()));
        return patient;
    }

    /** This method is used in order to represent Patient Application User in JSON format at register process.
     *
     * @param userPatientRepresentation
     * @return JSON Representation of Patient Application User data.
     */
    static public ApplicationUser getUser(UserPatientRepresentation userPatientRepresentation) {
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setUsername(userPatientRepresentation.getUsername());
        applicationUser.setPassword(userPatientRepresentation.getPassword());
        applicationUser.setRole(CustomRole.getRoleValueNumber(CustomRole.ROLE_PATIENT.getRoleNumber()));
        return applicationUser;
    }
}
