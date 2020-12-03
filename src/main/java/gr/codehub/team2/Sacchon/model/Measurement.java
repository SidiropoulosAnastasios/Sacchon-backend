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
public class Measurement {
    /**
     * This Class is used in order to construct Measurement table in database. Primary key id auto generated.
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private LocalDate date;
    private double glucoseLevel;
    private double carbIntake;

    public Measurement(LocalDate date, double glucoseLevel, double carbIntake) {
        this.date = date;
        this.glucoseLevel = glucoseLevel;
        this.carbIntake = carbIntake;
    }

    /**
     * Here we have the relational connections with Patient table.
     * Many measurements belong to a patient. Patient ID is used as Foreign Key for Measurement.
     */
    @ManyToOne
    @JoinColumn(name = "patientId")
    private Patient patient;

}
