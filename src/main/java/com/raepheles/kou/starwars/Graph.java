package com.raepheles.kou.starwars;

import java.util.*;

public class Graph {

    private int[][] coordinates;
    private Map<Location, List<Location>> adjVertices;

    public Graph(int[][] coordinates) {
        this.adjVertices = new HashMap<>();
        this.coordinates = coordinates;
    }

    public List<Location> getShortestPath(Location source, Location destination) {
        createGraph();
        int[] pred = new int[adjVertices.keySet().size()];
        int[] dist = new int[adjVertices.keySet().size()];
        boolean flagSource = false;
        boolean flagDest = false;
        for(Location loc: adjVertices.keySet()) {
            if(source.getX() == loc.getX() &&
                    source.getY() == loc.getY()) {
                source = loc;
                flagSource = true;
            }
            if(destination.getX() == loc.getX() &&
                    destination.getY() == loc.getY()) {
                destination = loc;
                flagDest = true;
            }
            if(flagDest && flagSource)
                break;
        }

        if(!BFS(source, destination, pred, dist)) {
            return new ArrayList<>();
        }

        List<Location> path = new ArrayList<>();
        Location crawl = new Location(destination.getId(), destination.getX(), destination.getY());
        path.add(crawl);
        while(pred[crawl.getId()] != -1) {
            Location temp = getLocationWithId(pred[crawl.getId()]);
            path.add(temp);
            crawl = temp;
        }
        return path;
    }

    private Location getLocationWithId(int id) {
        for(Location location: adjVertices.keySet()) {
            if(location.getId() == id)
                return location;
        }
        return null;
    }

    private boolean BFS(Location source, Location destination, int[] pred, int[] dist) {
        int size = adjVertices.keySet().size();
        Queue<Location> queue = new ArrayDeque<>();
        boolean[] visited = new boolean[size];
        for(int i = 0; i < size; i++) {
            visited[i] = false;
            dist[i] = Integer.MAX_VALUE;
            pred[i] = -1;
        }

        visited[source.getId()] = true;
        dist[source.getId()] = 0;
        queue.add(source);
        while(!queue.isEmpty()) {
            Location u = queue.poll();
            for(int i = 0; i < adjVertices.get(u).size(); i++) {
                int id = adjVertices.get(u).get(i).getId();
                if(!visited[id]) {
                    visited[id] = true;
                    dist[id] = dist[u.getId()] + 1;
                    pred[id] = u.getId();
                    queue.add(adjVertices.get(u).get(i));

                    if(id == destination.getId())
                        return true;
                }
            }
        }
        return false;
    }

    private Location getLocationFromMap(Location location) {
        for(Location loc: adjVertices.keySet()) {
            if(loc.getX() == location.getX() &&
                    loc.getY() == location.getY())
                return loc;
        }
        return null;
    }

    private void createGraph() {
        int size = 0;
        for(int i = 0; i < coordinates.length; i++) {
            for(int j = 0; j < coordinates[0].length; j++) {
                if(coordinates[i][j] == 1) {
                    Location temp = new Location(size++, i, j);
                    adjVertices.put(temp, new ArrayList<>());
                }
            }
        }
        for(Location location1: adjVertices.keySet()) {
            List<Location> tempList = new ArrayList<>();
            for(Location location2: adjVertices.keySet()) {
                // Location 2 is on the right
                if(location1.getY() == location2.getY() &&
                        location2.getX() == location1.getX()+1)
                    tempList.add(location2);
                // Location 2 is on the left
                else if(location1.getY() == location2.getY() &&
                        location2.getX() == location1.getX()-1)
                    tempList.add(location2);
                // Location 2 is on the top
                else if(location1.getX() == location2.getX() &&
                        location2.getY() == location1.getY()-1)
                    tempList.add(location2);
                // Location 2 is at the bottom
                else if(location1.getX() == location2.getX() &&
                        location2.getY() == location1.getY()+1)
                    tempList.add(location2);
            }
            adjVertices.put(location1, tempList);
        }
    }
}
