package edu.uoc.trip.model.cells;

import edu.uoc.trip.model.levels.Level;
import edu.uoc.trip.model.utils.Coordinate;

    public class Cell{
    public Cell(int row, int column, CellType type) {
        setCoordinate(row, column);
        setType(type);

    }
    private CellType type;
    private Coordinate coordinate;

    public CellType getType(){
        return this.type;
    }

    protected void setType(CellType type){
        this.type = type;

    }

    public Coordinate getCoordinate(){
        return this.coordinate;
    }

    protected void setCoordinate(int row, int column){
        this.coordinate = new Coordinate(row, column);

    }

    public boolean isMovable(){
        return false;
    }

    public boolean isRotatable(){
        return false;
    }


    public String toString(){
        return String.valueOf(type.getUnicodeRepresentation());

    }




}
