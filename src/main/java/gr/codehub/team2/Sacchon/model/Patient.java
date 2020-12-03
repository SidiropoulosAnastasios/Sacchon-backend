package gr.codehub.team2.Sacchon.model;

import gr.codehub.team2.Sacchon.common.DiabetesType;
import gr.codehub.team2.Sacchon.common.Gender;
import gr.codehub.team2.Sacchon.security.dao.ApplicationUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Patient {
    /**
     * This Class is used in order to construct Patient table in database. Primary key id auto generated.
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private String name;
    private LocalDate dob;
    private String gender;
    private String diabetesType;
    private LocalDate dateDiagnosed;
    private boolean active = true;
    private boolean readyForConsultation = false;

    public Patient(String name, LocalDate dob, String gender, String diabetesType, LocalDate dateDiagnosed) {
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.diabetesType = diabetesType;
        this.dateDiagnosed = dateDiagnosed;
    }

    /**
     * Here we have the relational connections through tables.
     * One Patient has many measurements.
     * One Patient has a doctor. Doctor ID is used as Foreign Key for Patient.
     * One Patient has many consultations.
     * One Patient has an account as Application User.
     */
    @OneToMany(mappedBy="patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Measurement> measurements;

    @ManyToOne
    @JoinColumn(name="doctorId")
    private Doctor doctor;

    @OneToMany(mappedBy="patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Consultation> consultations;

    @OneToOne(mappedBy = "patient",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ApplicationUser applicationUser;

}
