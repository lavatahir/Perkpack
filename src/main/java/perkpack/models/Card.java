package perkpack.models;
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
    private Set<User> users = new HashSet<>();

    @OneToMany
    private Collection<Perk> perks = new HashSet<>();

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

    public Collection<User> getUsers() {
        return users;
    }

    public void addUser(User u){
        users.add(u);
    }

    public void removeUser(User user){
        Iterator<User> itr = users.iterator();  // list is a Set<String>!
        while (itr.hasNext()) {
            User u = itr.next();
            if (user.equals(u)) {
                itr.remove();
            }
        }
    }

    public Collection<Perk> getPerks() {
        return perks;
    }

    public void addPerk(Perk p){
        perks.add(p);
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
                c.users.equals(this.users) &&
                c.perks.equals(this.perks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.description, this.name, this.perks, this.users);
    }
}
