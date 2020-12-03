package gr.codehub.team2.Sacchon.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Consultation {
    /**
     * This Class is used in order to construct Consultation table in database. Primary key id auto generated.
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private LocalDate date;
    private String medication;
    private String dosage;
    private String extraInfo;
    private boolean readByPatient = false;

    public Consultation(String medication, String dosage, String extraInfo) {
        this.date = LocalDate.now();
        this.medication = medication;
        this.dosage = dosage;
        this.extraInfo = extraInfo;
    }
    /**
     * Here we have the relational connections through tables.
     * Many Consultations belong to one Patient. Patient ID is used as Foreign Key for Consultation.
     * Many Consultations belong to one Doctor. Doctor ID is used as Foreign Key for Consultation.
     */
    @ManyToOne
    @JoinColumn(name = "patientId")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctorId")
    private Doctor doctor;

}
