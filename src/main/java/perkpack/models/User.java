package perkpack.models;

import org.hibernate.validator.constraints.Email;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
public class User {

    @NotNull
    @Size(min =1, max = 32)
    private String firstName;

    @NotNull
    @Size(min =1, max = 32)
    private String lastName;

    @NotNull
    @Email
    @Size(min =1, max = 32)
    private String email;

    @Id
    @GeneratedValue
    private long id;

    public User()
    {

    }

    public User(String firstName, String lastName, String email)
    {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
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
                && this.lastName == user.lastName
                && this.email == user.email
                && this.id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.firstName, this.lastName, this.id, this.email);
    }
}
