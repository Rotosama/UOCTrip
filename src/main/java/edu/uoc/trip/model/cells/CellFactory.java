package edu.uoc.trip.model.cells;

/**
 * Cell Simple Factory class.
 * @author David García Solórzano
 * @version 1.0
 */
public abstract class CellFactory {

    /**
     * Returns a new object which is the class (Cell, MovableCell or RotatableCell)
     * that the given CellType indicates.
     *
     * @param row Row of the coordinate/position in which the cell is in the board.
     * @param column Column of the coordinate/position in which the cell is in the board.
     * @param type Value of the enumeration called CellType that corresponds to the cell.
     * @return Cell object that is related to the "CellType".
     */
    public static Cell getCellInstance(int row, int column, CellType type){

        return switch (type) {
            case VERTICAL, HORIZONTAL, BOTTOM_RIGHT, BOTTOM_LEFT, TOP_RIGHT, TOP_LEFT, FREE -> new MovableCell(row, column, type);
            case ROTATABLE_VERTICAL, ROTATABLE_HORIZONTAL -> new RotatableCell(row, column,type);
            default -> new Cell(row, column, type);
        };
    }
}
