package perkpack.models;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable
    private Set<Account> accounts = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Perk> perks = new HashSet<>();

    public Card(String name, String description){
        this.name = name;
        this.description = description;
    }

    //Dummy constructor for json
    public Card(){
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<Account> getAccounts() {
        return accounts;
    }

    public void addUser(Account u){
        accounts.add(u);
    }

    public void removeUser(Account account){
        Iterator<Account> itr = accounts.iterator();  // list is a Set<String>!
        while (itr.hasNext()) {
            Account u = itr.next();
            if (account.equals(u)) {
                itr.remove();
            }
        }
    }

    public Collection<Perk> getPerks() {
        return perks;
    }

    public void addPerk(Perk p){
        perks.add(p);
        p.setCard(this);
    }

    public void removePerk(Perk p){
        Iterator<Perk> itr = perks.iterator();  // list is a Set<String>!
        while (itr.hasNext()) {
            Perk perk = itr.next();
            if (perk.equals(p)) {
                itr.remove();
            }
        }
    }

    @Override
    public boolean equals(Object o){
        if(o == null) return false;
        if(!(o instanceof Card)){
            return false;
        }
        Card c = (Card) o;

        return c.getName().equalsIgnoreCase(this.getName()) &&
                c.getDescription().equalsIgnoreCase(this.getDescription()) &&
                c.accounts.equals(this.accounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.description, this.name, this.perks, this.accounts);
    }
}
