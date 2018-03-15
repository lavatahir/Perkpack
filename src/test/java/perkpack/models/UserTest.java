package perkpack.models;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    private static String firstName = "ali";
    private static String lastName = "farah";
    private static String email = "ali@gmail.com";
    private static User user;

    @Before
    public  void setup()
    {
        user = new User(firstName,lastName,email,"password");
    }

    @Test
    public void getFirstNameTest() throws Exception {
        assertEquals(user.getFirstName(), firstName);
    }

    @Test
    public void setFirstNameTest() throws Exception {
        String name = "john";
        user.setFirstName(name);
        assertEquals(user.getFirstName(), name);
    }

    @Test
    public void getLastNameTest() throws Exception {
        assertEquals(user.getLastName(), lastName);
    }

    @Test
    public void setLastNameTest() throws Exception {
        String name = "john";
        user.setLastName(name);
        assertEquals(user.getLastName(), name);
    }

    @Test
    public void getEmailTest() throws Exception {
        assertEquals(user.getEmail(), email);
    }

    @Test
    public void setEmailTest() throws Exception {
        String email2 = "john@gmail.com";
        user.setEmail(email2);
        assertEquals(user.getEmail(), email2);
    }

    @Test
    public void validEqualsTest() throws Exception {
        User user2 = new User(firstName,lastName,email,"password");
        assertEquals(user,user2);
    }

    @Test
    public void invalidEqualsTest() throws Exception {
        User user2 = new User(firstName,lastName,"","password");
        assertNotEquals(user2,user);
    }
}