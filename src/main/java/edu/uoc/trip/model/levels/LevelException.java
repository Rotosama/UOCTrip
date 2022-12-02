package edu.uoc.trip.model.levels;

public class LevelException extends Exception {

    public static final String ERROR_PARSING_LEVEL_FILE= "[ERROR] There was an error while loading the current level file!!";
    public static final String ERROR_BOARD_SIZE= "[ERROR] Board's size must be greater than 2!!";
    public static final String ERROR_COORDINATE= "[ERROR] This coordinate is incorrect!!";
    public static final String ERROR_NO_STARTING= "[ERROR] This level does not have any starting cell!!";
    public static final String ERROR_NO_FINISH= "[ERROR] This level does not have any finish cell!!";
    public static final String ERROR_NO_ROAD= "[ERROR] This level does not have any road!!";
    public static final String ERROR_NO_MOVABLE_CELL= "[ERROR] You have chosen a static cell!!";
    public static final String ERROR_NO_ROTATABLE_CELL= "[ERROR] You have chosen a non-rotatable cel!!";

    public LevelException(String msg){
        super(msg);
    }


}
