package com.example.ashish.minesweeper;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ashish on 3/30/16.
 */
public class Cell implements Parcelable {


    String value;
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public boolean isBombPresent() {
        return isBombPresent;
    }

    public void setIsBombPresent(boolean isBombPresent) {
        this.isBombPresent = isBombPresent;
    }

    @Override
    public String toString() {
        return value + ": " + isBombPresent;
    }


    boolean isBombPresent;

    public  Cell(String currentValue, boolean isBomb){
        value = currentValue ;
        isBombPresent = isBomb;
    }

    private Cell(Parcel in) {
        value = in.readString();
        isBombPresent = (in.readInt() == 0) ? false : true;

    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(value);
        out.writeInt(isBombPresent ? 1 : 0);
    }

    public static final Parcelable.Creator<Cell> CREATOR = new Parcelable.Creator<Cell>() {
        public Cell createFromParcel(Parcel in) {
            return new Cell(in);
        }

        public Cell[] newArray(int size) {
            return new Cell[size];
        }
    };
}
