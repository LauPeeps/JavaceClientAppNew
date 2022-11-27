package com.example.javaceclientapp;

public class ModelLeaderboard {

    private String username;
    private String score;

    public ModelLeaderboard() {}


    public ModelLeaderboard(String username, String score) {
        this.username = username;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
