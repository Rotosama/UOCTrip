package edu.uoc.trip.model.cells;

import edu.uoc.trip.model.utils.Coordinate;

public class MovableCell extends Cell implements Movable {

    public MovableCell(int row, int column, CellType type) {
        super(row, column, type);

    }

    public boolean isMovable(){
        return true;

    }

    public boolean isRotatable(){
        return false;

    }

    public void move(Coordinate destination){
        setCoordinate(destination.getRow(), destination.getColumn());

    }
}
