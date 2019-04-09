package com.raepheles.kou.starwars.characters;

public class Luke extends PlayableCharacter {
    private double health;

    public Luke() {
        super("Luke");
        this.health = 3.0;
    }

    public void loseHealth() {
        health--;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }
}
