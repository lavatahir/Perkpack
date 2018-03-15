package perkpack.models;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class CardTest {

    private static String name = "Visa";
    private static String description = "Credit Card";
    private static User user = new User("Lava", "Tahir", "lava@gmail.com");
    private static Card card;

    @Before
    public  void setup()
    {
        card = new Card(name, description, user);
    }

    @Test
    public void getNameTest() throws Exception {
        assertEquals(card.getName(), name);
    }

    @Test
    public void getDescriptionTest() throws Exception {
        assertEquals(card.getDescription(), description);
    }

    @Test
    public void getCreatorTest() throws Exception {
        assertEquals(card.getCreator(), user);
    }

    @Test
    public void getPerksTest() throws Exception {
        Collection<Perk> emptyPerks = new HashSet<>();
        assertEquals(card.getPerks(), emptyPerks);
    }


    @Test
    public void addPerkTest() throws Exception {
        Perk p = new Perk();
        card.addPerk(p);
        assertTrue(card.getPerks().contains(p));
    }

    @Test
    public void checkEqualsTest() throws Exception {
        Card c = new Card(name, description, user);
        assertEquals(card, c);
    }

    @Test
    public void checkNotEqualsTest() throws Exception {
        Card c = new Card("Weirdo", description, user);
        assertNotEquals(card, c);
    }

}
