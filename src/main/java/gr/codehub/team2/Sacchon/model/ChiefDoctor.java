package gr.codehub.team2.Sacchon.model;

import gr.codehub.team2.Sacchon.security.dao.ApplicationUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ChiefDoctor {
    /**
     * This Class is used in order to construct ChiefDoctor table in database. Primary key id auto generated.
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private String name;

    public ChiefDoctor(String name) {
        this.name = name;
    }
    /**
     * Here we have the relational connections with Application User table.
     * One Chief Doctor has an Application User account.
     */
    @OneToOne(mappedBy = "chiefDoctor",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ApplicationUser applicationUser;
}
