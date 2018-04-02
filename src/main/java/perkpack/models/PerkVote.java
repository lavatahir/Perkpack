package perkpack.models;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class PerkVote {

    @NotNull
    private String name;

    @NotNull
    @Range(min = -1, max = 1)
    private int vote;

    @ManyToOne(cascade = CascadeType.ALL)
    private Account account;

    @OneToOne
    private Category category;

    @Id
    @GeneratedValue
    private long id;

    public PerkVote() {

    }

    public PerkVote(String name, int vote, Account account) {
        this.name = name;
        this.vote = vote;
        this.account = account;
    }

    public String getName() {
        return this.name;
    }

    public int getVote() {
        return this.vote;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVote(int setVote) {
        this.vote = setVote;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof PerkVote))
        {
            return false;
        }

        PerkVote perkVote = (PerkVote) o;

        return this.name.equals(perkVote.name) &&
                this.account.equals(perkVote.account) &&
                this.vote == perkVote.vote;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name,this.account, this.vote);
    }
}
