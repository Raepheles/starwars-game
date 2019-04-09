package com.raepheles.kou.starwars;

import com.raepheles.kou.starwars.characters.Character;
import com.raepheles.kou.starwars.characters.Luke;
import com.raepheles.kou.starwars.characters.PlayableCharacter;
import com.raepheles.kou.starwars.characters.Yoda;
import com.raepheles.kou.starwars.exceptions.BoardException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class StarWars extends Application {
    private static Button[][] buttons = new Button[11][14];
    private static Button buttonLuke = new Button("Play with Luke");
    private static Button buttonYoda = new Button("Play with Yoda");
    private static Button buttonOpenMap = new Button("Open Map");
    private static File mapFile = null;
    private static boolean gameActive = false;
    private static PlayableCharacter ourCharacter = null;
    private static Board board = null;
    private static int turn = 0;
    private static Label errorText = new Label("");
    private static Label turnLabel = new Label("");
    private static Label health = new Label("");
    private static boolean goalReached = false;
    private static boolean gameOver = false;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPrefWidth(800);
        root.setPrefHeight(600);

        GridPane gamePane = new GridPane();
        gamePane.setPrefWidth(600);
        gamePane.setPrefHeight(600);
        gamePane.getStyleClass().add("gamePane");


        for(int i = 0; i < 11; i++) {
            for(int j = 0; j < 14; j++) {
                Button button = new Button();
                button.setDisable(false);
                button.setPrefSize(40,40);
                buttons[i][j] = button;
                gamePane.add(buttons[i][j], j, i);
            }
        }

        GridPane menuPane = new GridPane();
        menuPane.setPrefWidth(200);
        menuPane.setPrefHeight(600);
        menuPane.setHgap(10);
        menuPane.setVgap(10);
        menuPane.setPadding(new Insets(10));
        menuPane.getStyleClass().add("menuPane");

        ColumnConstraints constraints = new ColumnConstraints();
        constraints.setHgrow(Priority.SOMETIMES);
        menuPane.getColumnConstraints().add(constraints);
        constraints = new ColumnConstraints();
        constraints.setHgrow(Priority.SOMETIMES);
        menuPane.getColumnConstraints().add(constraints);


        buttonOpenMap.setPrefWidth(menuPane.getPrefWidth()-20);
        buttonLuke.setPrefWidth(menuPane.getPrefWidth()-20);
        buttonYoda.setPrefWidth(menuPane.getPrefWidth()-20);

        buttonLuke.setDisable(true);
        buttonYoda.setDisable(true);


        Label healthText = new Label("Health: ");
        health.setPrefHeight(50);
        health.setPrefWidth(menuPane.getPrefWidth()-20);

        errorText.setTextFill(Color.RED);
        errorText.setWrapText(true);

        menuPane.add(buttonOpenMap, 0, 0);
        menuPane.add(buttonLuke, 0, 1);
        menuPane.add(buttonYoda, 0, 2);
        //menuPane.add(healthText, 0, 3);
        menuPane.add(health, 0, 3);
        menuPane.add(turnLabel, 0, 4);
        menuPane.add(errorText, 0, 5);

        root.setLeft(gamePane);
        root.setRight(menuPane);
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add("style.css");

        board = new Board();

        // Button Open Map
        buttonOpenMap.setOnAction(e -> {
            errorText.setText("");
            if(openFile(primaryStage)) {
                try {
                    board.loadBoard();
                } catch(BoardException be) {
                    errorText.setText(be.toString());
                    return;
                }
                buttonOpenMap.setDisable(true);
                buttonLuke.setDisable(false);
                buttonYoda.setDisable(false);
                gameOver = false;
                turn = 0;
                gameActive = true;
            } else {
                errorText.setText("There has been error while opening the file. Make sure it is a txt file.");
            }
        });

        // Starting the game (Yoda and Luke Buttons)
        buttonLuke.setOnAction(e -> {
            ourCharacter = new Luke();
            ourCharacter.setCurrentLocation(board.getOurInitialLocation());
            gameActive = true;
            refreshBoard();
            buttonLuke.setDisable(true);
            buttonYoda.setDisable(true);
        });
        buttonYoda.setOnAction(e -> {
            ourCharacter = new Yoda();
            ourCharacter.setCurrentLocation(board.getOurInitialLocation());
            gameActive = true;
            refreshBoard();
            buttonLuke.setDisable(true);
            buttonYoda.setDisable(true);
        });


        // Key Listener
        scene.setOnKeyReleased(e -> {
            if(!gameActive || goalReached || gameOver) {
                // Do nothing
                return;
            }
            boolean legitMove = false;
            switch (e.getCode()) {
                case W:
                    legitMove = move(Directions.UP);
                    break;
                case S:
                    legitMove = move(Directions.DOWN);
                    break;
                case D:
                    legitMove = move(Directions.RIGHT);
                    break;
                case A:
                    legitMove = move(Directions.LEFT);
                    break;
            }
            if(legitMove) {
                turn++;
                enemyTurn();
                refreshBoard();
            }
        });

        primaryStage.setTitle("Prolab 2 Pro 1 - Berk Yatkın");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private static void refreshBoard() {
        turnLabel.setText(String.valueOf(turn));

        if(goalReached) {
            for(int i = 0; i < 11; i++) {
                for(int j = 0; j < 14; j++) {
                    buttons[i][j].setDisable(true);
                }
            }
            errorText.setText("YOU WON!");
            return;
        }
        if(gameOver) {
            for(int i = 0; i < 11; i++) {
                for(int j = 0; j < 14; j++) {
                    buttons[i][j].setDisable(true);
                }
            }
            errorText.setText("YOU LOST!");
            return;
        }

        Location locationOurCharacter = ourCharacter.getCurrentLocation();
        List<Character> badCharacters = board.getBadCharacters();
        int[][] boardCoordinates = board.getCoordinates();
        // First change all buttons to black and while (depending on availability)
        for(int i = 0; i < 11; i++) {
            for(int j = 0; j < 14; j++) {
                buttons[i][j].setText("");
                int val = boardCoordinates[i][j];
                if(val == 0)
                    buttons[i][j].setStyle("-fx-background-color: #646464");
                else if(val == 1)
                    buttons[i][j].setStyle("-fx-background-color: #FFFFFF");
            }
        }
        // Add hp icons
        if(ourCharacter.getName().equalsIgnoreCase("luke")) {
            if(ourCharacter.getHealth() == 3.0) {
                health.getStyleClass().add("full-hp");
            } else if(ourCharacter.getHealth() == 2.0) {
                health.getStyleClass().remove("full-hp");
                health.getStyleClass().add("two-hp");
            } else if(ourCharacter.getHealth() == 1.0) {
                health.getStyleClass().remove("two-hp");
                health.getStyleClass().add("one-hp");
            }
        } else {
            if(ourCharacter.getHealth() == 6.0) {
                health.getStyleClass().add("full-hp");
            } else if(ourCharacter.getHealth() == 4.0) {
                health.getStyleClass().remove("full-hp");
                health.getStyleClass().add("two-hp");
            } else if(ourCharacter.getHealth() == 2.0) {
                health.getStyleClass().remove("two-hp");
                health.getStyleClass().add("one-hp");
            }
        }
        // Make the goal yellow
        buttons[board.getGoal().getX()][board.getGoal().getY()].setStyle("-fx-background-color: #F7FF00");
        // Our character icon
        if(ourCharacter.getName().equalsIgnoreCase("yoda"))
            buttons[locationOurCharacter.getX()][locationOurCharacter.getY()].setStyle("-fx-background-image: url(\"yoda.png\")");
        else
            buttons[locationOurCharacter.getX()][locationOurCharacter.getY()].setStyle("-fx-background-image: url(\"luke.png\")");
        // Bad character icons and their path towards our character
        for(Character badCharacter: badCharacters) {
            Location tempLocation = badCharacter.getCurrentLocation();
            if(badCharacter.getName().equalsIgnoreCase("darthvader")) {
                buttons[tempLocation.getX()][tempLocation.getY()].setStyle("-fx-background-image: url(\"darthvader.png\")");
            } else if(badCharacter.getName().equalsIgnoreCase("stormtrooper")) {
                buttons[tempLocation.getX()][tempLocation.getY()].setStyle("-fx-background-image: url(\"stormtrooper.png\")");
            } else if(badCharacter.getName().equalsIgnoreCase("kyloren")) {
                buttons[tempLocation.getX()][tempLocation.getY()].setStyle("-fx-background-image: url(\"kyloren.png\")");
            }
            List<Location> path = badCharacter.shortestPath(ourCharacter.getCurrentLocation());
            for(Location location: path) {
                if(location.getX() == ourCharacter.getCurrentLocation().getX() &&
                        location.getY() == ourCharacter.getCurrentLocation().getY())
                    continue;
                if(board.getCoordinates()[location.getX()][location.getY()] == 0) {
                    buttons[location.getX()][location.getY()].setStyle("-fx-background-color: #BB8200");
                } else {
                    boolean full = false;
                    for(Character character: badCharacters) {
                        if(character.getCurrentLocation().getX() == location.getX() &&
                                character.getCurrentLocation().getY() == location.getY())
                            full = true;
                    }
                    if(!full)
                        buttons[location.getX()][location.getY()].setStyle("-fx-background-color: #FFB200");
                }
            }
        }
    }

    private static boolean legitMoveExists(Location ourLocation) {
        return isLegitToMove(new Location(ourLocation.getX(), ourLocation.getY()+1)) ||
                isLegitToMove(new Location(ourLocation.getX(), ourLocation.getY()-1)) ||
                isLegitToMove(new Location(ourLocation.getX()+1, ourLocation.getY())) ||
                isLegitToMove(new Location(ourLocation.getX()-1, ourLocation.getY()));
    }

    private static boolean move(Directions directions) {
        Location current = ourCharacter.getCurrentLocation();
        Location destination;
        switch (directions) {
            case UP:
                destination = new Location(current.getX()-1, current.getY());
                if(isLegitToMove(destination)) {
                    ourCharacter.setCurrentLocation(destination);
                    errorText.setText("");
                    return true;
                } else {
                    errorText.setText("You have made an invalid move");
                }
                break;
            case RIGHT:
                destination = new Location(current.getX(), current.getY()+1);
                if(isLegitToMove(destination)) {
                    ourCharacter.setCurrentLocation(destination);
                    errorText.setText("");
                    return true;
                } else {
                    errorText.setText("You have made an invalid move");
                }
                break;
            case DOWN:
                destination = new Location(current.getX()+1, current.getY());
                if(isLegitToMove(destination)) {
                    ourCharacter.setCurrentLocation(destination);
                    errorText.setText("");
                    return true;
                } else {
                    errorText.setText("You have made an invalid move");
                }
                break;
            case LEFT:
                destination = new Location(current.getX(), current.getY()-1);
                if(isLegitToMove(destination)) {
                    ourCharacter.setCurrentLocation(destination);
                    errorText.setText("");
                    return true;
                } else {
                    errorText.setText("You have made an invalid move");
                }
                break;
        }
        return false;
    }

    private static void enemyTurn() {
        for(Character badCharacter: board.getBadCharacters()) {
            List<Location> path = badCharacter.shortestPath(ourCharacter.getCurrentLocation());
            if(path.isEmpty())
                return;
            if(path.get(0).getX() == ourCharacter.getCurrentLocation().getX() &&
                    path.get(0).getY() == ourCharacter.getCurrentLocation().getY()) {
                loseHealth();
            } else if(badCharacter.getName().equalsIgnoreCase("kyloren")) {
                if(path.get(1).getX() == ourCharacter.getCurrentLocation().getX() &&
                        path.get(1).getY() == ourCharacter.getCurrentLocation().getY())
                    loseHealth();
            }
            if(badCharacter.getName().equalsIgnoreCase("darthvader")) {
                int val = board.getCoordinates()[path.get(0).getX()][path.get(0).getY()];
                if(val == 0)
                    board.getCoordinates()[path.get(0).getX()][path.get(0).getY()] = 1;
            }
            if(badCharacter.getName().equalsIgnoreCase("kyloren"))
                badCharacter.setCurrentLocation(path.get(1));
            else
                badCharacter.setCurrentLocation(path.get(0));
        }
        if(!legitMoveExists(ourCharacter.getCurrentLocation())) {
            try {
                board.loadBoard();
            } catch(BoardException be) {
                be.printStackTrace();
                return;
            }
            ourCharacter.setCurrentLocation(board.getOurInitialLocation());
            ourCharacter.loseHealth();
            turn = 0;
            if(ourCharacter.getHealth() == 0) {
                gameOver = true;
            }
        }
    }

    private static void loseHealth() {
        try {
            board.loadBoard();
        } catch(BoardException be) {
            be.printStackTrace();
            return;
        }
        ourCharacter.setCurrentLocation(board.getOurInitialLocation());
        ourCharacter.loseHealth();
        turn = 0;
        if(ourCharacter.getHealth() == 0) {
            gameOver = true;
        }
    }

    private static boolean isLegitToMove(Location destination) {
        int x = destination.getX();
        int y = destination.getY();
        // Check if it is goal
        // Goal has to be on the side of the border (excluding corners)
        // There is no check for it here since it's pre-set in the code
        int goalX = board.getGoal().getX();
        int goalY = board.getGoal().getY();
        if(goalX == 0) {
            if(x == -1 && y == goalY)
                goalReached = true;
                return true;
        } else if(goalX == 10) {
            if(x == 11 && y == goalY)
                goalReached = true;
                return true;
        } else if(goalY == 0) {
            if(y == -1 && x == goalX) {
                goalReached = true;
                return true;
            }
        } else if(goalY == 13) {
            if(y == 14 && x == goalX) {
                goalReached = true;
                return true;
            }
        }
        // Out of boundaries
        if(x < 0 || x > 10 || y < 0 || y > 13) {
            return false;
        }
        // If there is a wall on the destination
        if(board.getCoordinates()[x][y] == 0) {
            return false;
        }
        // If there is a bad character on the destination
        List<Character> badCharacters = board.getBadCharacters();
        for(Character badCharacter: badCharacters) {
            Location location = badCharacter.getCurrentLocation();
            if(x == location.getX() && y == location.getY()) {
                return false;
            }
        }
        // If none of the above applies then it's free to move here
        return true;
    }

    public static Board getBoard() {
        return board;
    }

    public static File getMapFile() {
        return mapFile;
    }

    private boolean openFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if(file == null) {
            return false;
        } else if(file.getName().endsWith(".txt")) {
            mapFile = file;
            return true;
        } else {
            return false;
        }
    }
}
