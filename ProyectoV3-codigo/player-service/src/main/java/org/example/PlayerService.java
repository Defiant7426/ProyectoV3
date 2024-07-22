package org.example;

public class PlayerService {

    private int score;
    private int baseHealth;

    public PlayerService(){
        this.score = 0; // iniciamos con score 0
        this.baseHealth = 100; // iniciamos con vida 100
    }

    public int getScore() { // obtenemos el score actual
        return score;
    }

    public int getBaseHealth() { // obtenemos la vida base actual
        return baseHealth;
    }

}