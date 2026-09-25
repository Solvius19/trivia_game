package org.example.model;

public class Player {

    private int currentScore;
    private final String name;
    private int currentStreak;
    private int currentStreakMultiplier;

    public Player(String name) {
        this.name = name;
        this.currentScore = 0;
        this.currentStreak = 0;
        this.currentStreakMultiplier = 1;
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

    public int getCurrentStreak() {
        return currentStreak;
    }

    public int getCurrentStreakMultiplier() {
        return currentStreakMultiplier;
    }

    public void incrementStreak() {
        this.currentStreak++;
        if (this.currentStreak % 3 == 0) {
            this.currentStreakMultiplier++;
        }
    }

    public void resetStreak() {
        this.currentStreak = 0;
        this.currentStreakMultiplier = 1;
    }

    @Override
    public String toString() {
        return name + "'s turn. Current score: " +
                currentScore + "\nCurrent streak: " +
                currentStreak + " (Multiplier: x" +
                currentStreakMultiplier + ")";
    }
}
