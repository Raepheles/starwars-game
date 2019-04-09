package com.raepheles.kou.starwars.characters;

public abstract class PlayableCharacter extends Character {
    double health = 3;

    public PlayableCharacter(String name) {
        super(name);
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
