package gr.codehub.team2.Sacchon.representation;

import gr.codehub.team2.Sacchon.model.Consultation;
import gr.codehub.team2.Sacchon.model.Doctor;
import gr.codehub.team2.Sacchon.model.Patient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class ConsultationRepresentation {
    private String date;
    private String medication;
    private String dosage;
    private String extraInfo;
    private boolean readByPatient;
    private Long patientId;
    private Long doctorId;
    private Long id;

    /**
     * The URL of this resource.
     */
    private String uri;

    /** This method is used in order to to match Consultation JSON Representation to Consultation data using data from Doctor and Patient.
     *
     * @param consultationRepresentation
     * @param doctor
     * @param patient
     * @return Consultation data.
     */
    static public Consultation getConsultation(ConsultationRepresentation consultationRepresentation, Optional<Doctor> doctor, Optional<Patient> patient) {
        Consultation consultation = new Consultation();
        consultation.setDate(LocalDate.now());
        consultation.setMedication(consultationRepresentation.getMedication());
        consultation.setDosage(consultationRepresentation.getDosage());
        consultation.setExtraInfo(consultationRepresentation.getExtraInfo());
        consultation.setReadByPatient(consultationRepresentation.isReadByPatient());
        consultation.setDoctor(doctor.get());
        consultation.setPatient(patient.get());
        return consultation;
    }

    /** This method is used in order to represent Consultation in JSON format.
     *
     * @param consultation
     * @return JSON Representation of Consultation data.
     */
    static public ConsultationRepresentation getConsultationRepresentation(Consultation consultation) {
        ConsultationRepresentation consultationRepresentation = new ConsultationRepresentation();
        consultationRepresentation.setId(consultation.getId());
        consultationRepresentation.setDate(consultation.getDate().toString());
        consultationRepresentation.setMedication(consultation.getMedication());
        consultationRepresentation.setDosage(consultation.getDosage());
        consultationRepresentation.setExtraInfo(consultation.getExtraInfo());
        consultationRepresentation.setReadByPatient(consultation.isReadByPatient());
        consultationRepresentation.setPatientId(consultation.getPatient().getId());
        consultationRepresentation.setDoctorId(consultation.getDoctor().getId());
        consultationRepresentation.setUri("http://localhost:9000/sacchon/patients/" + consultation.getPatient().getId() + "/consultations/" + consultation.getId());
        return consultationRepresentation;
    }

}
