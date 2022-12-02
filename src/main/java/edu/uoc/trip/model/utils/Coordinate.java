package edu.uoc.trip.model.utils;

import java.util.Objects;

public class Coordinate {


    private int row, column;

    public Coordinate(int row, int column) {
        setRow(row);
        setColumn(column);
    }

    public int getRow() {
        return row;
    }

    private void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    private void setColumn(int column) {
        this.column = column;
    }

    @Override
    public boolean equals(Object obj){
        if (obj==null || (!(obj instanceof Coordinate))){
            return false;
        }
        Coordinate coord = (Coordinate) obj;

        return this.getRow() == coord.getRow() && this.getColumn() == coord.getColumn();
    }

    @Override
    public int hashCode(){
       return Objects.hash(this.getRow(),this.getColumn());
    }

    @Override
    public String toString(){
        return "(" + getRow() + "," + getColumn() + ")";
    }
}
