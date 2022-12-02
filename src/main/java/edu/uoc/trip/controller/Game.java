package edu.uoc.trip.controller;

import edu.uoc.trip.model.cells.Cell;
import edu.uoc.trip.model.levels.Level;
import edu.uoc.trip.model.levels.LevelDifficulty;
import edu.uoc.trip.model.levels.LevelException;
import edu.uoc.trip.model.utils.Coordinate;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Objects;

/**
 * Controller class of the game. It is the middleware (or bridge) between the model and view classes.
 * <br/>
 * This class is called from the view classes in order to access/modify the model data.
 *
 *  @author David García-Solórzano
 *  @version 1.0
 */
public class Game {

    /**
     * Name of the folder in which level files are
     */
    private String fileFolder;

    /**
     * Number of the current level.
     */
    private int currentLevel = 0;

    /**
     * Maximum quantity of levels that the game has.
     */
    private final int maxLevels;

    /**
     * Level object that contains the information of the current level.
     */
    private Level level;

    /**
     * Constructor
     *
     * @param fileFolder Folder name where the configuration/level files are.
     * @throws IOException When there is a problem while retrieving number of levels
     */
    public Game(String fileFolder) throws IOException {
        int num;

        setFileFolder(fileFolder);

        //Get the number of files that are in the fileFolder, i.e. the number of levels.

        URL url = getClass().getClassLoader().getResource(getFileFolder());

        URLConnection urlConnection = Objects.requireNonNull(url).openConnection();

        if(urlConnection instanceof JarURLConnection){
            //run in jar
            String path = null;
            try {
                path = getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            } catch (URISyntaxException e) {
                System.out.println("ERROR: Game Constructor");
                e.printStackTrace();
                System.exit(-1);
            }

            URI uri = URI.create("jar:file:"+path);

            try(FileSystem fs = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
                num = (int) Files.walk(fs.getPath(getFileFolder()))
                        .filter(Files::isRegularFile).count();
            }
        }else{
            //run in ide
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream;
            inputStream = Objects.requireNonNull(classLoader.getResourceAsStream(getFileFolder()));

            try(InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(streamReader)){
                num = (int) reader.lines().count();
            }
        }
            maxLevels = num;
    }

    /**
     * Setter of the attribute "fileFolder".
     *
     * @param fileFolder Folder name where the configuration/level files are.
     */
    private void setFileFolder(String fileFolder){
        this.fileFolder = fileFolder;
    }

    /**
     * Getter of the attribute "fileFolder".
     *
     * @return Value of the attribute "fileFolder".
     */
    private String getFileFolder(){
        return fileFolder;
    }

    /**
     * Returns the number of rows and columns of the board of the current level. The board is an NxN square.
     *
     * @return The number of rows and columns of the board of the current level.
     */
    public int getBoardSize() {
        if(level==null)
            return 0;

        return level.getSize();
    }

    /**
     * Returns the difficulty of the current level.
     *
     * @return The difficulty of the current level.
     */
    public LevelDifficulty getDifficulty() {
        if(level==null)
            return LevelDifficulty.STARTER;

        return level.getDifficulty();
    }

    /**
     * Returns the number of moves that have been done to solve the current level.
     *
     * @return Number of moves that the player has done to solve the current level.
     */
    public int getNumMoves() {
        if(level==null)
            return 0;

        return level.getNumMoves();
    }

    /**
     * Indicates if the game is finished (true) or not (false).
     * The game is finished when the attribute "currentLevel" is equals to attribute "maxLevels".
     *
     * @return True if there are no more levels and therefore the game is finished. Otherwise, false.
     */
    private boolean isFinished() {
        //TODO
        if (currentLevel == maxLevels){
            return true;
        }
        return false;
    }

    /**
     * Getter of the attribute "currentLevel".
     * @return Value of the attribute "currentLevel" that indicates which level we are playing.
     */
    public int getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Checks if there is a new level to play and loads it.<br/>
     * If the game is finished, it returns false. Otherwise, it returns true.
     *
     * @return True if there is a next level, and it has been loaded correctly. Otherwise, it returns false.
     * @throws LevelException When there is a level exception/problem.
     */
    public boolean nextLevel() throws LevelException {
        //TODO
        if (isFinished()){
            return false;
        }else{
            currentLevel ++;
            loadLevel();
            return true;
        }

    }

    /**
     * Loads a new level by using the value of attribute "currentLevel".
     *
     * The pattern of the filename is: fileFolder+"level" + numberLevel + ".txt".
     * @throws LevelException When there is a level exception/problem.
     */
    private void loadLevel() throws LevelException {
        //TODO
        level = new Level(getFileFolder()+"level" + getCurrentLevel() + ".txt");
    }

    /**
     * Checks if the level is solved, i.e. there is a path from the starting cell to the ending cell.
     *
     * @return true if this level is beaten, otherwise false.
     * @throws LevelException When there is a level exception/problem.
     */
    public boolean isLevelSolved() throws LevelException {
        if(level==null)
            return false;
        return level.isSolved();
    }

    /**
     * Returns the cell that is in the given coordinate.
     *
     * @param coord Coordinate of the cell that we want to retrieve.
     * @return Cell which is the position/coordinate "coord".
     * @throws LevelException When the coordinate is invalid.
     */
    public Cell getCell(Coordinate coord) throws LevelException{
        return level.getCell(coord);
    }

    /**
     * Returns the cell that is in the given (row,col).
     *
     * @param row Row of the cell that we want to retrieve.
     * @param col Column of the cell that we want to retrieve.
     * @return Cell which is the position/coordinate  (row,col).
     * @throws LevelException When the coordinate is invalid.
     */
    public Cell getCell(int row, int col) throws LevelException{
        return getCell(new Coordinate(row, col));
    }

    /**
     * Returns a String with the board of the current level in textual format.
     *
     * @return Text-based board of the current level.
     */
    public String getBoardText() {
        return level.toString();
    }

    /**
     * Swaps the cells that are in the two given coordinates.
     *
     * @param first Object representing the coordinate of the first cell.
     * @param second Object representing the coordinate of the second cell.
     * @throws LevelException When any coordinate is incorrect or the swap was unsuccessful.
     */
    public void swap(Coordinate first, Coordinate second) throws LevelException {
        level.swapCells(first,second);
    }

    /**
     * Rotates the cell which is in the coordinate "cellCoord".
     *
     * @param cellCoord Object representing the coordinate of the cell.
     * @throws LevelException When the coordinate is incorrect or the cell in the coordinate cannot be rotated.
     */
    public void rotate(Coordinate cellCoord) throws LevelException{
       level.rotateCell(cellCoord);
    }

    /**
     * Reloads the current level, i.e. load the level again.
     *
     * @throws LevelException When there is a level exception/problem.
     */
    public void reload() throws LevelException {
        //TODO
        loadLevel();
    }
}
