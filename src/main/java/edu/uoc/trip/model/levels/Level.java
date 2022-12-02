package edu.uoc.trip.model.levels;

import edu.uoc.trip.model.cells.*;
import edu.uoc.trip.model.utils.Coordinate;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Class that represents each level of the game.
 *
 * @author David García-Solórzano
 * @version 1.0
 */
public class Level {

    /**
     * Size of the board, i.e. size x size.
     */
    private int size;

    /**
     * Difficulty of the level
     */
    private LevelDifficulty difficulty;

    /**
     * Representation of the board.
     */
    private Cell[][] board;

    /**
     * Number of moves that the player has made so far.
     */
    private int numMoves = 0;

    /**
     * Minimum value that must be assigned to the attribute "size".
     */
    private static final int MINIMUM_BOARD_SIZE = 3;

    /**
     * Constructor
     *
     * @param fileName Name of the file that contains level's data.
     * @throws LevelException When there is any error while parsing the file.
     */
    public Level(String fileName) throws LevelException {
        setNumMoves(0);
        parse(fileName);
    }

    /**
     * Parses/Reads level's data from the given file.<br/>
     * It also checks which the board's requirements are met.
     *
     * @param fileName Name of the file that contains level's data.
     * @throws LevelException When there is any error while parsing the file
     * or some board's requirement is not satisfied.
     */
    private void parse(String fileName) throws LevelException{
        boolean isStarting = false;
        boolean isFinish = false;
        String line;

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = Objects.requireNonNull(classLoader.getResourceAsStream(fileName));

        try(InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader)){

            line = getFirstNonEmptyLine(reader);

            if (line  != null) {
                setSize(Integer.parseInt(line));
            }

            line = getFirstNonEmptyLine(reader);

            if (line != null) {
                setDifficulty(LevelDifficulty.valueOf(line));
            }

            board = new Cell[getSize()][getSize()];

            for (int row = 0; row < getSize(); row++) {
                char[] rowChar = Objects.requireNonNull(getFirstNonEmptyLine(reader)).toCharArray();
                for (int column = 0; column < getSize(); column++) {
                    board[row][column] = CellFactory.getCellInstance(row, column,
                            Objects.requireNonNull(CellType.map2CellType(rowChar[column])));
                }
            }

        }catch (IllegalArgumentException | IOException e){
            throw new LevelException(LevelException.ERROR_PARSING_LEVEL_FILE);
        }

        //Check if there is one starting cell, one finish cell and, at least, any other type of cell.
        for(var j =0; j<getSize(); j++){

            if(getCell(new Coordinate(getSize()-1,j)).getType() == CellType.START){
                isStarting = true;
            }

            if(getCell(new Coordinate(0,j)).getType() == CellType.FINISH){
                isFinish = true;
            }
        }

        //Checks if there are more than one starting cell
        if(Stream.of(board).flatMap(Arrays::stream).filter(x -> x.getType() == CellType.START).count()>1){
            throw new LevelException(LevelException.ERROR_PARSING_LEVEL_FILE);
        }

        //Checks if there are more than one finish cell
        if(Stream.of(board).flatMap(Arrays::stream).filter(x -> x.getType() == CellType.FINISH).count()>1){
            throw new LevelException(LevelException.ERROR_PARSING_LEVEL_FILE);
        }

        if(!isStarting){
            throw new LevelException(LevelException.ERROR_NO_STARTING);
        }

        if(!isFinish){
            throw new LevelException(LevelException.ERROR_NO_FINISH);
        }

        //Checks if there is one road (i.e. movable or rotatable cell) at least.
        if(Stream.of(board).flatMap(Arrays::stream).noneMatch(x -> x.isMovable() || x.isRotatable())){
            throw new LevelException(LevelException.ERROR_NO_ROAD);
        }

    }

    /**
     * This a helper method for {@link #parse(String fileName)} which returns
     * the first non-empty and non-comment line from the reader.
     *
     * @param br BufferedReader object to read from.
     * @return First line that is a parsable line, or {@code null} there are no lines to read.
     * @throws IOException if the reader fails to read a line.
     */
    private String getFirstNonEmptyLine(final BufferedReader br) throws IOException {
        do {

            String s = br.readLine();

            if (s == null) {
                return null;
            }
            if (s.isBlank() || s.startsWith("#")) {
                continue;
            }

            return s;
        } while (true);
    }

    public int getSize() {
        return size;
    }

    private void setSize(int size) throws LevelException{
        if (size < 3){
            throw new LevelException(LevelException.ERROR_BOARD_SIZE);
        }
        this.size = size;
    }

    public LevelDifficulty getDifficulty(){
        return this.difficulty;
    }

    private void setDifficulty(LevelDifficulty difficulty){
        this.difficulty = difficulty;
    }

    public int getNumMoves(){
        return numMoves;
    }

    private void setNumMoves(int numMoves){
        this.numMoves = numMoves;
    }

    private boolean validatePosition(Coordinate coordinate){
        if (coordinate == null){
            return false;
        }
        boolean a = coordinate.getRow() >= 0 && coordinate.getRow() < board.length
                && coordinate.getColumn() >= 0 && coordinate.getColumn() < board.length;
        return coordinate.getRow() >= 0 && coordinate.getRow() < board.length
                && coordinate.getColumn() >= 0 && coordinate.getColumn() < board.length;
    }

    public Cell getCell(Coordinate coord) throws LevelException{
        if (!validatePosition(coord)){
            throw new LevelException(LevelException.ERROR_COORDINATE);
        }
        return board[coord.getRow()][coord.getColumn()];
    }

    private void setCell(Coordinate coord, Cell cell) throws LevelException{
        if (cell == null || !validatePosition(coord)){
            throw new LevelException(LevelException.ERROR_COORDINATE);
        }
        board[coord.getRow()][coord.getColumn()] = cell;
    }

    public void swapCells( Coordinate firstCoord, Coordinate secondCoord) throws LevelException{
        Cell firstCell = getCell(firstCoord);
        Cell secondCell = getCell(secondCoord);
        Cell auxCell = getCell(secondCoord);
        if (firstCell.isMovable() && secondCell.isMovable()){
            board[firstCoord.getRow()][firstCoord.getColumn()] = secondCell;
            board[auxCell.getCoordinate().getRow()][auxCell.getCoordinate().getColumn()] = firstCell;

            ((MovableCell) secondCell).move(firstCoord);
            ((MovableCell) firstCell).move(secondCoord);

            numMoves++;
        }else{
            throw new LevelException(LevelException.ERROR_NO_MOVABLE_CELL);
        }
    }

    public void rotateCell(Coordinate coord) throws LevelException {
        Cell cell = getCell(coord);
        if (cell.isRotatable()){
            RotatableCell rotatableCell = (RotatableCell) cell;
            rotatableCell.rotate();

            numMoves ++;
        }else{
            throw new LevelException(LevelException.ERROR_NO_ROTATABLE_CELL);
        }

    }

    public boolean isSolved(){
        int row = board.length-1;
        int column = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[row][i].getType() == CellType.START){
                column = i;
            }
        }
        Direction direction = Direction.UP;
        EnumSet<Direction> nextDir;
        while (board[row][column].getType() != CellType.FINISH){
            nextDir = board[row][column].getType().getAvailableConnections();

                if(nextDir.toArray().length == 1){
                    direction = (Direction) nextDir.toArray()[0];
                }else if(nextDir.toArray().length > 0){

                    if(nextDir.toArray()[0] == direction.getOpposite()){
                        direction = (Direction) nextDir.toArray()[1];

                    }else if(nextDir.toArray()[1] == direction.getOpposite()){
                        direction = (Direction) nextDir.toArray()[0];

                    }else{
                        return false;
                    }

                }else{
                    return false;
                }
                row = row + direction.getDRow();
                column = column + direction.getDColumn();

                if (!validatePosition(new Coordinate(row, column))){
                    return false;
                }
        }
        return true;
    }

    public String toString(){

        String stringMap = "  ";

        for (int i = 0; i < board.length; i++) {
            stringMap += i+1;
        }
        stringMap += System.lineSeparator();

        for (int row = 0; row < board.length; row++) {
            stringMap += (char) (97+row) + "|";
            for (int column = 0; column < board.length; column++) {
                stringMap += board[row][column].toString();
            }
            stringMap += System.lineSeparator();
        }
        return stringMap;
    }


}
