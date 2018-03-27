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

    public Long getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Category)) {
            return false;
        }

        Category c = (Category) o;

        return c.name.equalsIgnoreCase(this.name);
    }

    public String toString() {
        return this.name;
    }
}
