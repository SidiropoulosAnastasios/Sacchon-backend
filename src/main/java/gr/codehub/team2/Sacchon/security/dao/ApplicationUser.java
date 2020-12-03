package gr.codehub.team2.Sacchon.security.dao;

import gr.codehub.team2.Sacchon.model.ChiefDoctor;
import gr.codehub.team2.Sacchon.model.Doctor;
import gr.codehub.team2.Sacchon.model.Patient;
import gr.codehub.team2.Sacchon.security.CustomRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints =
@UniqueConstraint(columnNames = {"username"}))
public class ApplicationUser {
    @Id
    private String username; //must be unique
    private String password;
    private CustomRole role;
    private boolean active = true;

    /**
     * Here we have the relational connections through tables.
     * One Application User can be Patient, Doctor or ChiefDoctor.
     * Application User can have only one Foreign Key not null depending on role.
     */
    @OneToOne
    @JoinColumn(name = "patientId", referencedColumnName = "id")
    private Patient patient;

    @OneToOne
    @JoinColumn(name = "doctorId", referencedColumnName = "id")
    private Doctor doctor;

    @OneToOne
    @JoinColumn(name = "chiefDoctorId", referencedColumnName = "id")
    private ChiefDoctor chiefDoctor;
}