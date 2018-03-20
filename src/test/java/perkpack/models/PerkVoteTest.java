package perkpack.models;

import org.junit.Test;
import static org.junit.Assert.*;

public class PerkVoteTest {

    private static final User voter = new User("Ali","farah", "a@gmail.com", "password");
    private static final int score = 1;
    private static final String  perkName = "perk";

    private PerkVote perkVote = new PerkVote(perkName,score,voter);;


    @Test
    public void getName() {
        assertEquals(perkName, perkVote.getName() );
    }

    @Test
    public void getVote() {
        assertEquals(score, perkVote.getVote() );
    }

    @Test
    public void getVoter() {
        assertEquals(voter, perkVote.getUser() );
    }

    @Test
    public void equals() {
        PerkVote perkVote1 = new PerkVote(perkName, score,voter);
        assertEquals(perkVote, perkVote1);
    }

    @Test
    public void notEquals() {
        PerkVote perkVote1 = new PerkVote("name2", -1,voter);
        assertNotEquals(perkVote, perkVote1);
    }
}

