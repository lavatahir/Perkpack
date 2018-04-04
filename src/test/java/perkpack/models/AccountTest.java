package perkpack.models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountTest {

    private static String firstName = "ali";
    private static String lastName = "farah";
    private static String email = "ali@gmail.com";
    private static Account account;

    @Before
    public  void setup()
    {
        account = new Account(firstName,lastName,email,"password");
    }

    @Test
    public void getFirstNameTest() throws Exception {
        assertEquals(account.getFirstName(), firstName);
    }

    @Test
    public void setFirstNameTest() throws Exception {
        String name = "john";
        account.setFirstName(name);
        assertEquals(account.getFirstName(), name);
    }

    @Test
    public void getLastNameTest() throws Exception {
        assertEquals(account.getLastName(), lastName);
    }

    @Test
    public void setLastNameTest() throws Exception {
        String name = "john";
        account.setLastName(name);
        assertEquals(account.getLastName(), name);
    }

    @Test
    public void getEmailTest() throws Exception {
        assertEquals(account.getEmail(), email);
    }

    @Test
    public void setEmailTest() throws Exception {
        String email2 = "john@gmail.com";
        account.setEmail(email2);
        assertEquals(account.getEmail(), email2);
    }

    @Test
    public void validEqualsTest() throws Exception {
        Account account2 = new Account(firstName,lastName,email,"password");
        assertEquals(account, account2);
    }

    @Test
    public void invalidEqualsTest() throws Exception {
        Account account2 = new Account(firstName,lastName,"","password");
        assertNotEquals(account2, account);
    }

    @Test
    public void topCategoryAdditionTest() throws Exception {
        Account account2 = new Account(firstName, lastName, email, "password");
        PerkVote perkVote = new PerkVote("TestPerk", 1, account2);
        PerkVote perkVote2 = new PerkVote("TestPerk2", 1, account2);
        PerkVote perkVote3 = new PerkVote("TestPerk3", 1, account2);
        Category perkCategory = new Category("Category");
        Category perkCategory2 = new Category("Category2");

        perkVote.setCategory(perkCategory);
        perkVote2.setCategory(perkCategory2);
        perkVote3.setCategory(perkCategory2);

        account2.addVote(perkVote);

        assertEquals(account2.getTopCategory(), perkCategory);

        account2.addVote(perkVote2);
        account2.addVote(perkVote3);

        assertEquals(account2.getTopCategory(), perkCategory2);
    }

    @Test
    public void topCategoryRemovalTest() throws Exception {
        Account account2 = new Account(firstName, lastName, email, "password");
        PerkVote perkVote = new PerkVote("TestPerk", 1, account2);
        PerkVote perkVote2 = new PerkVote("TestPerk2", 1, account2);
        PerkVote perkVote3 = new PerkVote("TestPerk3", 1, account2);
        Category perkCategory = new Category("Category");
        Category perkCategory2 = new Category("Category2");

        perkVote.setCategory(perkCategory);
        perkVote2.setCategory(perkCategory2);
        perkVote3.setCategory(perkCategory2);

        account2.addVote(perkVote2);
        account2.addVote(perkVote3);

        assertEquals(account2.getTopCategory(), perkCategory2);

        account2.addVote(perkVote);

        assertEquals(account2.getTopCategory(), perkCategory2);

        account2.removeVote(perkVote2);
        account2.removeVote(perkVote3);

        assertEquals(account2.getTopCategory(), perkCategory);
    }
}