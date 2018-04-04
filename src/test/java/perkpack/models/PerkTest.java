package perkpack.models;

import org.junit.Before;
import org.junit.Test;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class PerkTest {
    private static String name = "10% off Coffee";
    private static Date expiryDate = new Date();
    private static String location = "Tim Hortons (Carleton University)";
    private static String description = "Applies to all types of coffee";
    private static Category category = new Category("Food");
    private static Perk longPerk, shortPerk;

    @Before
    public void setup() {
        longPerk = new Perk(name, expiryDate, location, description, category);
        shortPerk = new Perk(name, description, category);
    }

    @Test
    public void getNameTest() {
        assertEquals(shortPerk.getName(), name);
        assertEquals(longPerk.getName(), name);
    }

    @Test
    public void getDescriptionTest() {
        assertEquals(shortPerk.getDescription(), description);
        assertEquals(longPerk.getDescription(), description);
    }

    @Test
    public void setAndGetScoreTest() {
        shortPerk.setScore(5);
        longPerk.setScore(-5);

        assertEquals(shortPerk.getScore(), 5);
        assertEquals(longPerk.getScore(), -5);
    }

    @Test
    public void getCategoryTest() {
        assertEquals(shortPerk.getCategory(), category);
        assertEquals(longPerk.getCategory(), category);
    }

    @Test
    public void setNameTest() {
        Perk testPerk = new Perk("tempname", "n/a", category);

        assertEquals(testPerk.getName(), "tempname");
        testPerk.setName(name);
        assertEquals(testPerk.getName(), name);
    }

    @Test
    public void setDescriptionTest() {
        Perk testPerk = new Perk("tempname", "n/a", category);

        assertEquals(testPerk.getDescription(), "n/a");
        testPerk.setDescription(description);
        assertEquals(testPerk.getDescription(), description);
    }

    @Test
    public void setCategoryTest() {
        Category testCategory = new Category("tempcategory");
        Perk testPerk = new Perk("tempname", "n/a", testCategory);

        assertEquals(testPerk.getCategory(), testCategory);
        testPerk.setCategory(category);
        assertEquals(testPerk.getCategory(), category);
    }

    @Test
    public void setCardBelongsToTest() {
        Card c = new Card("SPC", "Student card");
        Category testCategory = new Category("tempcategory");
        Perk testPerk = new Perk("tempname", "n/a", testCategory);

        testPerk.setCardPerkBelongsTo(c);

        assertEquals(testPerk.getCardPerkBelongsTo(), c);
    }
}
