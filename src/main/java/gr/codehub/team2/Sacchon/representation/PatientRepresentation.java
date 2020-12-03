package gr.codehub.team2.Sacchon.representation;

import gr.codehub.team2.Sacchon.model.Patient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class PatientRepresentation {
    private String name;
    private String dob;
    private String gender;
    private String diabetesType;
    private String dateDiagnosed;
    private boolean active = true;
    private boolean readyForConsultation = false;
    private Long id;
    private Long days;

    /**
     * The URL of this resource.
     */
    private String uri;

    /** This method is used in order to match Patient JSON Representation to Patient data.
     *
     * @param patientRepresentation
     * @return Patient data.
     */
    static public Patient getPatient(PatientRepresentation patientRepresentation) {
        Patient patient = new Patient();
        patient.setName(patientRepresentation.getName());
        patient.setDob(LocalDate.parse(patientRepresentation.getDob()));
        patient.setGender(patientRepresentation.getGender());
        patient.setDiabetesType(patientRepresentation.getDiabetesType());
        patient.setDateDiagnosed(LocalDate.parse(patientRepresentation.getDateDiagnosed()));
        return patient;
    }

    /** his method is used in order to represent Patient in JSON format.
     *
     * @param patient
     * @return JSON Representation of Patient data.
     */
    static public PatientRepresentation getPatientRepresentation(Patient patient){
        PatientRepresentation patientRepresentation = new PatientRepresentation();
        patientRepresentation.setId(patient.getId());
        patientRepresentation.setName(patient.getName());
        patientRepresentation.setDob(patient.getDob().toString());
        patientRepresentation.setGender(patient.getGender());
        patientRepresentation.setDiabetesType(patient.getDiabetesType());
        patientRepresentation.setDateDiagnosed(patient.getDateDiagnosed().toString());
        patientRepresentation.setActive(patient.isActive());
        patientRepresentation.setReadyForConsultation(patient.isReadyForConsultation());
        patientRepresentation.setUri("http://localhost:9000/sacchon/patients/"+patient.getId());
        return patientRepresentation;
    }
    public static PatientRepresentation getPatientsList(Patient patient, long timeElapsed) {
        PatientRepresentation patientRepresentation = new PatientRepresentation();
        patientRepresentation.setName(patient.getName());
        patientRepresentation.setId(patient.getId());
        patientRepresentation.setName(patient.getName());
        patientRepresentation.setDob(patient.getDob().toString());
        patientRepresentation.setGender(patient.getGender());
        patientRepresentation.setDiabetesType(patient.getDiabetesType());
        patientRepresentation.setDateDiagnosed(patient.getDateDiagnosed().toString());
        patientRepresentation.setDays(timeElapsed);
        return patientRepresentation;
    }

}