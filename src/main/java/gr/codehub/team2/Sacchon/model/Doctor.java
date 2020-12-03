package gr.codehub.team2.Sacchon.model;

import gr.codehub.team2.Sacchon.security.dao.ApplicationUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Doctor {
    /**
     * This Class is used in order to construct Doctor table in database. Primary key id auto generated.
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private String name;
    private String doctorPermissionCode;
    private boolean active = true;

    public Doctor(String name, String doctorPermissionCode) {
        this.name = name;
        this.doctorPermissionCode =doctorPermissionCode;
    }
    /**
     * Here we have the relational connections through tables.
     * One Doctor has many patients. We use set because patients are unique for every doctor.
     * One Doctor has many consultations.
     * One Doctor has an account as Application User.
     */
    @OneToMany(mappedBy="doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Patient> patients;

    @OneToMany(mappedBy="doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Consultation> consultations;

    @OneToOne(mappedBy = "doctor",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ApplicationUser applicationUser;
}
