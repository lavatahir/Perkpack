package perkpack.models;

import org.hibernate.validator.constraints.Email;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
public class User {

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @NotNull
    @Size(min =1, max = 32)
    private String firstName;

    @NotNull
    @Size(min =1, max = 32)
    private String lastName;

    @NotNull
    @Email
    @Size(min =1, max = 32)
    private String email;

    @NotNull
    private String password;

    @Id
    @GeneratedValue
    private long id;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_vote")
    private Set<PerkVote> votes = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "cards_and_users")
    private Set<Card> cards = new HashSet<>();

    public User()
    {

    }

    public User(String firstName, String lastName, String email, String password)
    {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.setPassword(password);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() { return password; }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }

    public long getId() {
        return id;
    }

    public Set<PerkVote> getVotes() {
        return votes;
    }

    public Optional<PerkVote> getVoteForPerk(PerkVote pendingVote)
    {
        if(this.votes.contains(pendingVote))
            return Optional.of(pendingVote);

        for(PerkVote vote: this.votes)
        {
            if(vote.getName().equals(pendingVote.getName()))
            {
                return Optional.of(vote);
            }
        }

        return Optional.empty();
    }

    public boolean removeVote(PerkVote vote)
    {
        return this.votes.remove(vote);
    }

    public boolean addVote(PerkVote vote)
    {
        return this.votes.add(vote);
    }

    public Set<Card> getCards() {
        return cards;
    }

    public boolean addCard(Card cardToAdd) {
        return this.cards.add(cardToAdd);
    }

    public boolean removeCard(Card cardToRemove){
        return this.cards.remove(cardToRemove);
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof User))
        {
            return false;
        }

        User user = (User) o;

        return this.email == user.email
                && this.firstName == user.firstName
                && this.lastName == user.lastName
                && this.id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.firstName, this.lastName, this.id, this.email, this.password);
    }
}
