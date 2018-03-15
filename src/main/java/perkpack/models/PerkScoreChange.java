package perkpack.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PerkScoreChange {
    private String name;

    @NotNull
    @Size(min =-1, max = 1)
    private int scoreChange;

    public PerkScoreChange() {

    }

    public PerkScoreChange(String name, int scoreChange) {
        this.name = name;
        this.scoreChange = scoreChange;
    }

    public String getName() {
        return this.name;
    }

    public int getScoreChange() {
        return this.scoreChange;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int scoreChange) {
        this.scoreChange = scoreChange;
    }
}
