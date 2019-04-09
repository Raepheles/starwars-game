package com.raepheles.kou.starwars;

public class Location {
    private int x;
    private int y;
    private int id;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
        this.id = -1;
    }

    public Location(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }

}
