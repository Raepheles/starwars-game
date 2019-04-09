package com.raepheles.kou.starwars;

import com.raepheles.kou.starwars.exceptions.LocationException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Utilities {

    public static List<String> readLinesFromFile(File file) throws IOException {
        return Files.readAllLines(file.toPath());
    }

    // Predefined gates
    public static Location getLocationOfTheGate(String gate) throws LocationException {
        switch (gate) {
            case "A":
                return new Location(5, 0);
            case "B":
                return new Location(0, 4);
            case "C":
                return new Location(0, 12);
            case "D":
                return new Location(5, 13);
            case "E":
                return new Location(10, 4);
            default:
                throw new LocationException("Out of boundaries.");
        }
    }

    public static boolean isPassable(int x1, int y1) {
        if(x1 >= 0 && x1 <= 10 && y1 >= 0 && y1 <= 13) {
            return StarWars.getBoard().getCoordinates()[x1][y1] == 1;
        } else {
            return false;
        }
    }
}
