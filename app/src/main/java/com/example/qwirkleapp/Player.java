package com.example.qwirkleapp;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;
    private int score;
    private List<String> scoreHistory; // Historique des scores

    public Player(String name) {
        this.name = name;
        this.score = 0;  // Score initialisé à 0
        this.scoreHistory = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<String> getScoreHistory() {
        return scoreHistory;
    }

    // Méthode pour ajouter ou soustraire des points
    public void addScore(int points) {
        score += points;
        String action = (points >= 0 ? "+" : "") + points;
        scoreHistory.add(action); // Ajouter l'action à l'historique
    }
}
