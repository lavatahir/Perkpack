package perkpack.models;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Perk {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id = null;

    private String name;
    private Date expiryDate;
    private String location;
    private String description;
    private int score;

    @ManyToOne
    private User creator;
    // private Product product;

    public Perk () {

    }

    public Perk (String name, Date expiryDate, String location, String description) {
        this.name = name;
        this.expiryDate = expiryDate;
        this.location = location;
        this.description = description;

        score = 0;
    }

    public Perk (String name, String description) {
        this(name, new Date(), "", description);
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return (name + " - " + description);
    }
}
