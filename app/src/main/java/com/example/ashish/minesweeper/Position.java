package com.example.ashish.minesweeper;

/**
 * Created by Ashish on 3/30/16.
 */
public class Position {
    int row;
    int column;

    Position(int newRow, int newColumn) {
       row = newRow;
        column = newColumn;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
