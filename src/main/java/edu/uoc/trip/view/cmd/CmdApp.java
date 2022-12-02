package edu.uoc.trip.view.cmd;

import edu.uoc.trip.controller.Game;
import edu.uoc.trip.model.cells.RotatableCell;
import edu.uoc.trip.model.levels.LevelException;
import edu.uoc.trip.model.utils.Coordinate;

import java.io.IOException;
import java.util.Scanner;

/**
 * Class that controls the interaction in the textual/command view.
 *
 * @author David García-Solórzano
 * @version 1.0
 */
public class CmdApp {

    /**
     * Game object that allows to manage the game.
     */
    Game game;

    /**
     * Initializes a new game with the folder which contains
     * the levels' configuration files and with the game mode "turns".
     *
     * @throws IOException When there is a problem while loading the game.
     */
    public CmdApp() throws IOException {
        this.game = new Game("levels/");
    }

    /**
     * Manages the idle process of the game.
     *
     * @throws LevelException When there is a level problem.
     */
    public void launchGame() throws LevelException {
        Scanner sc = new Scanner(System.in);
        Coordinate coordinateStarting;
        Coordinate coordinateEnding;

        while(game.nextLevel()) {
            System.out.println("LEVEL "+game.getCurrentLevel()+ " - "+ game.getDifficulty());
            // print board and keep accepting moves until game is over
            while (!game.isLevelSolved()) {
                System.out.println("Moves done: "+game.getNumMoves());
                System.out.println(game.getBoardText());
                System.out.println("Enter starting cell (row,col), e.g. a3: ");
                try{
                    coordinateStarting = coordinateFromInput(sc.nextLine());

                    //If it is a rotatable cell, then we don't ask for a second coordinate/cell
                    if(game.getCell(coordinateStarting) instanceof RotatableCell){
                        game.rotate(coordinateStarting);
                    }else{
                        System.out.println("Enter destination cell (row,col): ");
                        coordinateEnding = coordinateFromInput(sc.nextLine());
                        game.swap(coordinateStarting, coordinateEnding);
                    }
                }catch(LevelException e){
                    System.err.println(e.getMessage());
                    System.out.println("Please, try again!");
                }
                System.out.println();
            }

            System.out.println(game.getBoardText());
            System.out.println("Congrats!! You have solved Level "+game.getCurrentLevel()+" with "+game.getNumMoves()+" moves!!");
            System.out.println("Press enter to continue...");
            sc.nextLine();
        }

        sc.close();
    }

    /**
     * Transform a user input (String) in the corresponding Coordinate object.
     *
     * @param input User input (captured from the keyboard) with the format rowColumn, e.g. "a1".
     * @return Coordinate object that corresponds to the user input.
     * @throws LevelException When the input format or the coordinate is incorrect.
     */
    private Coordinate coordinateFromInput(String input) throws LevelException{
        char x = input.toLowerCase().charAt(1);
        char y = input.toLowerCase().charAt(0);

        if(input.length()!=2) throw new LevelException(LevelException.ERROR_COORDINATE);

        if(!Character.isLetter(y) //no letter
                || !(y >= 'a' && y < 97 +  game.getBoardSize()))
            throw new LevelException(LevelException.ERROR_COORDINATE);

        if(!Character.isDigit(x) //no number
            || !(x >= 49 && x < 49 +  game.getBoardSize())
        ) throw new LevelException(LevelException.ERROR_COORDINATE);

        return new Coordinate(((int)y - 97), ((int)x-49));
    }

    /**
     * Main method: entry point of the program when Gradle's "runCmdVersion" is used.
     *
     * @param args This parameter is not needed.
     */
    public static void main(String[] args) {
        System.out.println("Starting...");
        try {
            CmdApp cmd = new CmdApp();
            cmd.launchGame();
        } catch (IOException | LevelException e) {
            e.printStackTrace();
        }

        System.out.println("Finishing... bye!!");
    }
}
