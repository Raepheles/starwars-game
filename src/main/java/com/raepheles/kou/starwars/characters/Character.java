package com.raepheles.kou.starwars.characters;

import com.raepheles.kou.starwars.Graph;
import com.raepheles.kou.starwars.Location;
import com.raepheles.kou.starwars.StarWars;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Character {
    private String name;
    private Location currentLocation;
    private List<Location> pastMoves;

    public Character(String name) {
        this.name = name;
        pastMoves = new ArrayList<>();
    }

    public List<Location> shortestPath(Location destination) {
        Graph graph = new Graph(StarWars.getBoard().getCoordinates());
        List<Location> path = graph.getShortestPath(currentLocation, destination);
        if(path == null)
            return new ArrayList<>();
        Collections.reverse(path);
        path.remove(0);
        return path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Location> getPastMoves() {
        return pastMoves;
    }

    public void setPastMoves(List<Location> pastMoves) {
        this.pastMoves = pastMoves;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
}
