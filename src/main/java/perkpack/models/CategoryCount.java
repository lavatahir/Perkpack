package perkpack.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class CategoryCount {
    @Id
    @GeneratedValue
    private long id;

    @NotNull
    private Integer count;

    public CategoryCount() {

    }

    public CategoryCount(int count) {
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void decrementCount() {
        this.count -= 1;
    }

    public void incrementCount() {
        this.count += 1;
    }
}
