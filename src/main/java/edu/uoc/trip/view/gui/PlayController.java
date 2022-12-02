package edu.uoc.trip.view.gui;

import edu.uoc.trip.controller.Game;
import edu.uoc.trip.model.cells.Cell;
import edu.uoc.trip.model.cells.MovableCell;
import edu.uoc.trip.model.cells.RotatableCell;
import edu.uoc.trip.model.levels.LevelException;
import edu.uoc.trip.model.utils.Coordinate;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Class that controls the interaction in the Play view.
 *
 * @author David García-Solórzano
 * @version 1.0
 */
public class PlayController {

    /**
     * Game object that allows to manage the game.
     */
    private Game game;

    /**
     * It allows us to know when the player has clicked two cells.
     */
    private List<Coordinate> move;

    /**
     * Size of each cell in the board.
     */
    private static final int CELL_SIZE = 126;

    /**
     * It connects to the UI item that displays the board.
     */
    @FXML
    private Pane canvas;

    /**
     * It connects to the UI item so that the difficulty of level is displayed.
     */
    @FXML
    Label uiDifficulty;

    /**
     * It connects to the UI item so that the number of level is displayed.
     */
    @FXML
    Label uiLevel;

    /**
     * It connects to the UI item so that the number of moves the player has made is displayed.
     */
    @FXML
    Label uiMoves;

    /**
     * It allows us to manage the Alert message which displays "Congrats" when a level is solved.
     */
    private Alert alert;


    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     *
     * @throws IOException When there is a problem while loading the game.
     * @throws LevelException When there is a problem while loading a new level.
     */
    @FXML
    private void initialize() throws IOException, LevelException {
        move = new ArrayList<>();
        game = new Game("levels/");
        alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText("Congratulations!");
        if(game.nextLevel()) update();
    }

    /**
     * Updates the status of the level (i.e. the flow of the game). It also paints the game in the GUI.
     *
     * @throws LevelException When there is a level exception/problem.
     */
    private void update() throws LevelException {
        if(!game.isLevelSolved()) {
            paint();
        }else {
            //Level solved, then we show an alert (popup) window.
            paint();
            uiMoves.setText(String.valueOf(game.getNumMoves()));
            alert.setContentText("You have solved Level "+game.getCurrentLevel()+"!!");
            alert.showAndWait();
            if(!game.nextLevel()) {
                try {
                    GuiApp.main.createView("GameOver");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(2);
                }
            }else {
                paint();
            }
        }
    }
    /**
     * Paints the level in the GUI.
     *
     * @throws LevelException When the coordinate of a cell is invalid.
     */
    private void paint() throws LevelException{
        ObservableList<Node> nodeList = FXCollections.observableArrayList();
        canvas.getChildren().clear();

        uiDifficulty.setText(game.getDifficulty().toString());

        uiLevel.setText("Level " + game.getCurrentLevel());

        uiMoves.setText(String.valueOf(game.getNumMoves()));

        for (int i = 0; i < game.getBoardSize(); i++) {
            for (int j = 0; j < game.getBoardSize(); j++){
                Cell cell = game.getCell(i,j);
                StackPane sprite = new StackPane();

                ImageView spriteImage = new ImageView(new Image("/images/"+ cell.getType().getImageSrc()));

                spriteImage.setFitWidth(CELL_SIZE);
                spriteImage.setFitHeight(CELL_SIZE);
                sprite.getChildren().add(spriteImage);
                sprite.setTranslateX(CELL_SIZE * cell.getCoordinate().getColumn());
                sprite.setTranslateY(CELL_SIZE * cell.getCoordinate().getRow());

                if (cell instanceof MovableCell) {
                    sprite.getStyleClass().add("piece-movable");
                }

                if (!(cell instanceof MovableCell)){
                    sprite.getStyleClass().add("piece-movable-destination");
                }

                nodeList.addAll(sprite);

                sprite.setOnMouseClicked(e -> onClick(cell.getCoordinate()));
            }
        }

        canvas.getChildren().addAll(nodeList);

    }

    /**
     * Manages the click event, when a coordinate is clicked.
     * <br/><br/>
     * If the first coordinate that has been clicked contains a rotatable piece,
     * then it updates the status of the level by rotating the piece.
     * <br/><br/>
     * When 2 coordinates has been clicked and the pieces in both coordinates
     * are movable, then it updates the status of the level by swapping the pieces in the given coordinates.
     *
     * @param coord Coordinate object that corresponds to the cell of the board
     *              that has been clicked.
     */
    private void onClick(Coordinate coord){
        Optional<Node> nodeOptional = canvas.getChildren().stream()
                .filter(n -> n.getTranslateX()/CELL_SIZE==coord.getColumn() && n.getTranslateY()/CELL_SIZE == coord.getRow())
                .findFirst();

        if(nodeOptional.isPresent()){
            Node node = nodeOptional.get();
            node.getStyleClass().add("clicked");

            try {
                if(move.isEmpty() && game.getCell(coord) instanceof RotatableCell){
                    game.rotate(coord);
                    update();
                }else if(game.getCell(coord) instanceof MovableCell
                        && !move.contains(coord)){
                    move.add(coord);

                    if(move.size()==2) {
                        game.swap(move.get(0),move.get(1));
                        update();
                        move.clear();
                    }
                }else{
                    move.clear();
                    node.getStyleClass().removeAll("clicked");
                }
            } catch (LevelException e){
                move.clear();
                node.getStyleClass().removeAll("clicked");
            }
        }
    }
    @FXML
    private void onReload() throws FileNotFoundException, LevelException{
        game.reload();
        update();
    }
}
