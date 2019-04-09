package com.raepheles.kou.starwars.exceptions;

public class BoardException extends Exception {
    private String message;

    public BoardException(String message) {
        this.message = message;
    }

    public String toString() {
        return "Board exception: " + message;
    }
}
