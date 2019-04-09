package com.raepheles.kou.starwars.exceptions;

public class LocationException extends Exception {
    private String message;

    public LocationException(String message) {
        this.message = message;
    }

    public String toString() {
        return "Board exception: " + message;
    }
}
