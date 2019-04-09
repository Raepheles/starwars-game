package com.raepheles.kou.starwars.characters;

import com.raepheles.kou.starwars.Location;

import java.util.ArrayList;
import java.util.List;

public class DarthVader extends Character {

    public DarthVader() {
        super("DarthVader");
    }

    @Override
    public List<Location> shortestPath(Location destination) {
        List<Location> path = new ArrayList<>();
        Location current = this.getCurrentLocation();
        if(current.getX() > destination.getX()) {
            Location temp = new Location(current.getX()-1, current.getY());
            while(temp.getX() >= destination.getX()) {
                path.add(temp);
                temp = new Location(temp.getX()-1, temp.getY());
            }
            temp = new Location(temp.getX()+1, temp.getY());
            if(temp.getY() > destination.getY()) {
                temp = new Location(temp.getX(), temp.getY()-1);
                while(temp.getY() >= destination.getY()) {
                    path.add(temp);
                    temp = new Location(temp.getX(), temp.getY()-1);
                }
            } else {
                temp = new Location(temp.getX(), temp.getY()+1);
                while(temp.getY() <= destination.getY()) {
                    path.add(temp);
                    temp = new Location(temp.getX(), temp.getY()+1);
                }
            }
        } else {
            Location temp = new Location(current.getX()+1, current.getY());
            while(temp.getX() <= destination.getX()) {
                path.add(temp);
                temp = new Location(temp.getX()+1, temp.getY());
            }
            temp = new Location(temp.getX()-1, temp.getY());
            if(temp.getY() > destination.getY()) {
                temp = new Location(temp.getX(), temp.getY()-1);
                while(temp.getY() <= destination.getY()) {
                    path.add(temp);
                    temp = new Location(temp.getX(), temp.getY()-1);
                }
            } else {
                temp = new Location(temp.getX(), temp.getY()+1);
                while(temp.getY() <= destination.getY()) {
                    path.add(temp);
                    temp = new Location(temp.getX(), temp.getY()+1);
                }
            }
        }
        return path;
    }
}
