package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    // private User creator;
    // private Product product;

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
}