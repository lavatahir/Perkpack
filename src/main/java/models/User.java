package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class User {

    private String firstName;

    private String lastName;

    private String email;

    @Id
    @GeneratedValue
    private long id;

    public User()
    {

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

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return id;
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
                && this.lastName == user.email
                && this.id == user.id
                && this.id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.firstName, this.lastName, this.id, this.email);
    }
}
