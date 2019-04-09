package com.raepheles.kou.starwars;

import com.raepheles.kou.starwars.characters.Character;
import com.raepheles.kou.starwars.characters.DarthVader;
import com.raepheles.kou.starwars.characters.KyloRen;
import com.raepheles.kou.starwars.characters.Stormtrooper;
import com.raepheles.kou.starwars.exceptions.BoardException;
import com.raepheles.kou.starwars.exceptions.LocationException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Board {

    private int[][] coordinates;
    private boolean loaded;
    private Location ourInitialLocation; // This is the first location our character starts but this value is only used to pass it to our character object.
    private Location goal; // The real goal is to go beyond this point outside of the board
    private List<Character> badCharacters;

    public Board() {
        loaded = false;
        coordinates = new int[11][14];
        //Predefined location for our user
        ourInitialLocation = new Location(5, 6);
        goal = new Location(9, 13);
    }

    public Board(Location playerLocation) {
        loaded = false;
        coordinates = new int[11][14];
        //Predefined location for our user
        ourInitialLocation = playerLocation;
        goal = new Location(9, 13);
    }

    public void loadBoard() throws BoardException {
        File mapFile = StarWars.getMapFile();
        if(mapFile == null) {
            throw new BoardException("Attempted to load null board file.");
        }
        List<String> boardLines;
        try {
            boardLines = Utilities.readLinesFromFile(mapFile);
        } catch(IOException e) {
            throw new BoardException("Error loading board file.");
        }

        List<Character> badCharacters = new ArrayList<>();
        boolean startedParsingBoard = false;
        int j = 0;


        for(String line: boardLines) {
            if(line.startsWith("Character")) {
                if(startedParsingBoard) {
                    throw new BoardException("Invalid format. Please check your board file.");
                }
                String[] parts = line.split(",", 2);
                if(parts.length != 2) {
                    throw new BoardException("Error parsing board file.");
                }
                parts[0] = parts[0].replaceAll("Character:", "");
                switch (parts[0]) {
                    case "DarthVader":
                        badCharacters.add(new DarthVader());
                        break;
                    case "KyloRen":
                        badCharacters.add(new KyloRen());
                        break;
                    case "Stormtrooper":
                        badCharacters.add(new Stormtrooper());
                        break;
                    default:
                        throw new BoardException("Error parsing character name (" + parts[0] + ")");
                }
                parts[1] = parts[1].replaceAll("Gate:", "");
                Location startingPointOfBadCharacter;
                try {
                    startingPointOfBadCharacter = Utilities.getLocationOfTheGate(parts[1]);
                } catch(LocationException e) {
                    throw new BoardException("Location out of boundaries.");
                }
                badCharacters.get(badCharacters.size()-1).setCurrentLocation(startingPointOfBadCharacter);
            } else {
                startedParsingBoard = true;
                if(badCharacters.isEmpty()) {
                    throw new BoardException("There must be at least 1 character in board file.");
                }
                String temp = line.replaceAll("\\t", "");
                if(temp.length() != 14) {
                    throw new BoardException("Board must be 11 x 14.");
                }
                for(int i = 0; i < temp.length(); i++) {
                    int k;
                    char currentChar = temp.charAt(i);
                    if(currentChar == '0')
                        k = 0;
                    else if(currentChar == '1')
                        k = 1;
                    else
                        throw new BoardException("Board locations can only be 0 or 1.");

                    this.coordinates[j][i] = k;
                }
                j++;
            }
        }
        // If there is a wall our character's place change it to 1
        coordinates[ourInitialLocation.getX()][ourInitialLocation.getY()] = 1;

        // Successfully loaded the board
        this.badCharacters = badCharacters;
        this.loaded = true;
    }

    // Breaks the wall on the location (changes it to 0)
    public void breakWall(Location location) {
        coordinates[location.getX()][location.getY()] = 0;
    }

    // For debugging
    public void printBoard() {
        for(int i = 0; i < coordinates.length; i++) {
            for(int j = 0; j < coordinates[0].length; j++) {
                System.out.print("" + coordinates[i][j] + "\t");
            }
            System.out.print("\n");
        }
        for(Character character: badCharacters) {
            System.out.println(String.format("%s %s", character.getName(), character.getCurrentLocation()));
        }
        System.out.println("Our location: " + ourInitialLocation);
    }

    public List<Character> getBadCharacters() {
        return badCharacters;
    }

    public int[][] getCoordinates() {
        return coordinates;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public Location getOurInitialLocation() {
        return ourInitialLocation;
    }

    public Location getGoal() {
        return goal;
    }
}
