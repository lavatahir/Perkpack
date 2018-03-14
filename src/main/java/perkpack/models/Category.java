package perkpack.models;

import javax.persistence.*;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id = null;

    @Column(unique = true)
    private String name;

    public Category() {

    }

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Category) || o == null) {
            return false;
        }

        Category c = (Category) o;

        return c.name.equalsIgnoreCase(this.name);
    }
}
