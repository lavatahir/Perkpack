package perkpack.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;

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

    private String categoryName;

    @ManyToOne
    @JoinColumn(name = "card_perk")
    @JsonIgnore
    private Card cardPerkBelongsTo;

    public Perk () {

    }

    public Perk (String name, Date expiryDate, String location, String description, Category category) {
        this.name = name;
        this.expiryDate = expiryDate;
        this.location = location;
        this.description = description;
        this.category = category;
        this.categoryName = category.getName();

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

    public String getCategoryName() {
        return this.categoryName;
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

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public boolean vote(PerkVote pendingVote, Account voter)
    {
        pendingVote.setAccount(voter);
        Optional<PerkVote> optionalPerkVote = voter.getVoteForPerk(pendingVote);

        if(optionalPerkVote.isPresent())
        {
            PerkVote castedVote = optionalPerkVote.get();

            voter.removeVote(castedVote);

            if (castedVote.getVote() == pendingVote.getVote())
            {
                // if the voter votes in the same direction then undo the vote
                this.score += castedVote.getVote() * -1;
            }
            else
            {
                castedVote.setVote(castedVote.getVote());
                voter.addVote(pendingVote);

                this.score += castedVote.getVote() * -2;
            }
        }
        else
        {
            this.score += pendingVote.getVote();

            voter.addVote(pendingVote);
        }

        return true;
    }

    public void setCategory(Category category) {
        this.category = category;
        this.categoryName = category.getName();
    }

    public Card getCardPerkBelongsTo() {
        return cardPerkBelongsTo;
    }

    public void setCardPerkBelongsTo(Card card) {
        this.cardPerkBelongsTo = card;
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
