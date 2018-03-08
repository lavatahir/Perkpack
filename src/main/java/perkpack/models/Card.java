package perkpack.models;
import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    @ManyToOne
    private User creator;
    @OneToMany
    private Collection<Perk> perks;

    public Card(String name, String description, User creator){
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.perks = new HashSet<>();
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

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Collection<Perk> getPerks() {
        return perks;
    }

    public void setPerks(Collection<Perk> perks) {
        this.perks = perks;
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
                c.creator.equals(this.creator) &&
                c.perks.equals(this.perks);
    }
}
