package perkpack.models;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
public class Perk {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id = null;

    @Column(unique = true)
    private String name;

    private Date expiryDate;
    private String location;
    private String description;
    private int score;

    @ManyToOne
    private Category category;

    public Perk () {

    }

    public Perk (String name, Date expiryDate, String location, String description, Category category) {
        this.name = name;
        this.expiryDate = expiryDate;
        this.location = location;
        this.description = description;
        this.category = category;

        score = 0;
    }

    public Perk (String name, String description, Category category) {
        this(name, new Date(), "", description, category);
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public int getScore() {
        return this.score;
    }

    public Long getId() { return this.id; }

    public Category getCategory() {
        return this.category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean vote(PerkVote pendingVote, User voter)
    {
        pendingVote.setUser(voter);
        Optional<PerkVote> optionalPerkVote = voter.getVoteForPerk(pendingVote);

        if(optionalPerkVote.isPresent())
        {
            PerkVote castedVote = optionalPerkVote.get();
            if(castedVote.getVote() == pendingVote.getVote())
            {
                return false;
            }
            else if(pendingVote.getVote() == 0)
            {
                // reverse the previous vote
                this.score += (castedVote.getVote() * -1);

            }
            else {
                if(castedVote.getVote() == 0)
                    this.score += pendingVote.getVote();
                else
                    this.score += pendingVote.getVote() * 2;

            }
            voter.removeVote(castedVote);
        }
        else {
            this.score += pendingVote.getVote();
        }

        voter.addVote(pendingVote);
        return true;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String toString() {
        return (name + " - " + description + " - " + category.toString());
    }

    public boolean equals(Object o){
        if (!(o instanceof Perk)){
            return false;
        }

        Perk p = (Perk) o;

        return p.name.equalsIgnoreCase(this.name) &&
                p.description.equalsIgnoreCase(this.description) &&
                p.location.equalsIgnoreCase(this.location) &&
                p.score == this.score &&
                p.expiryDate.equals(this.expiryDate);
    }
}
