package org.example;

public class Player {

    private int currentScore;
    private final String name;

    public Player(String name) {
        this.name = name;
        this.currentScore = 0;
    }

    public void addScore(int value) {
        this.currentScore += value;
    }

    public void subtractScore(int value) {
        if (value > this.currentScore) {
            this.currentScore = -200;
        } else {
            this.currentScore -= value;
        }
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public String getName() {
        return name;
    }

    public void setScore(int score) {
        this.currentScore = score;
    }
}
