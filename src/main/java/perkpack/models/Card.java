package perkpack.models;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;

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

    @Override
    public boolean equals(Object o){
        if(o == null) return false;
        if(!(o instanceof Card)){
            return false;
        }
        Card c = (Card) o;

        return c.getName().equalsIgnoreCase(this.getName()) &&
                c.getDescription().equalsIgnoreCase(this.getDescription());
    }
}
