package perkpack.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
public class Account {
    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @NotNull
    @Size(min = 1, max = 32)
    private String firstName;

    @NotNull
    @Size(min = 1, max = 32)
    private String lastName;

    @NotNull
    @Email
    @Column(unique = true)
    @Size(min = 1, max = 32)
    private String email;

    @NotNull
    private String password;

    @Id
    @GeneratedValue
    private long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_vote")
    private Set<PerkVote> votes = new HashSet<>();

    @ManyToMany(mappedBy = "accounts")
    @JsonIgnore
    private Set<Card> cards = new HashSet<>();

    private ArrayList<Long> cardIds = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private Map<Category, CategoryCount> categoryCount = new HashMap<Category, CategoryCount>();

    @ManyToOne
    private Category topCategory;

    public Account()
    {

    }

    public Account(String firstName, String lastName, String email, String password) {
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

    public Category getTopCategory() {
        return topCategory;
    }

    public void setTopCategory(Category topCategory) {
        this.topCategory = topCategory;
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

    public boolean removeVote(PerkVote vote) {
        if (categoryCount.get(vote.getCategory()).getCount() == 1) {
            categoryCount.remove(vote.getCategory());
        } else {
            CategoryCount count = categoryCount.get(vote.getCategory());
            count.decrementCount();

            categoryCount.put(vote.getCategory(), count);
        }

        updateTopCategory();

        return this.votes.remove(vote);
    }

    public boolean addVote(PerkVote vote) {
        if (categoryCount.containsKey(vote.getCategory())) {
            CategoryCount count = categoryCount.get(vote.getCategory());
            count.incrementCount();

            categoryCount.put(vote.getCategory(), count);
        } else {
            categoryCount.put(vote.getCategory(), new CategoryCount(1));
        }

        updateTopCategory();

        return this.votes.add(vote);
    }

    public Set<Card> getCards() {
        return cards;
    }

    public boolean addCard(Card cardToAdd) {
        this.cardIds.add(cardToAdd.getId());
        return this.cards.add(cardToAdd);
    }

    public boolean removeCard(Card cardToRemove){
        this.cardIds.remove(cardToRemove.getId());
        return this.cards.remove(cardToRemove);
    }

    public ArrayList<Long> getCardIds() {
        return cardIds;
    }

    public void setCardIds(ArrayList<Long> cardIds) {
        this.cardIds = cardIds;
    }

    private void updateTopCategory() {
        if (categoryCount.size() == 0) {
            topCategory = null;

            return;
        }

        List<Map.Entry<Category, CategoryCount>> topList = new ArrayList<Map.Entry<Category, CategoryCount>>(categoryCount.entrySet());
        topList.sort((o1, o2) -> o2.getValue().getCount().compareTo(o1.getValue().getCount()));

        topCategory = topList.get(0).getKey();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Account))
        {
            return false;
        }

        Account account = (Account) o;

        return this.email == account.email
                && this.firstName == account.firstName
                && this.lastName == account.lastName
                && this.id == account.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.firstName, this.lastName, this.id, this.email, this.password);
    }

    @Override
    public String toString() {
        return (this.firstName + " " + this.lastName + " " + this.id + " " + this.email);
    }
}
