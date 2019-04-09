package com.raepheles.kou.starwars.characters;

public class Yoda extends PlayableCharacter {
    private double health;

    public Yoda() {
        super("Yoda");
        this.health = 6.0;
    }

    public void loseHealth() {
        health = health - 1;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }
}
