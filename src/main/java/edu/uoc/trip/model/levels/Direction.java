package edu.uoc.trip.model.levels;

public enum Direction {
    UP(-1,0,2),
    RIGHT(0,1,3),
    DOWN(1,0,0),
    LEFT(0,-1,1);

    private final int dRow,dColumn, opposite;

    Direction(int dRow, int dColumn, int opposite) {
        this.dRow = dRow;
        this.dColumn = dColumn;
        this.opposite = opposite;
    }

    public static Direction getValueByIndex(int index){
        return Direction.values()[index];
    }

    public int getDRow(){
        return this.dRow;
    }

    public int getDColumn() {
        return this.dColumn;
    }

    public Direction getOpposite() {
        return getValueByIndex(this.opposite);
    }
}
