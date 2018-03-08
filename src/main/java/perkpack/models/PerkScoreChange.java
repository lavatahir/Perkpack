package perkpack.models;

public class PerkScoreChange {
    private String name;
    private int score;

    public PerkScoreChange() {

    }

    public PerkScoreChange(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return this.name;
    }

    public int getScore() {
        return this.score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
