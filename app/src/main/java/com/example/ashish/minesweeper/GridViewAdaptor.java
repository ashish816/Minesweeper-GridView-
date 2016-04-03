package com.example.ashish.minesweeper;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ashish on 3/30/16.
 */
public class GridViewAdaptor extends BaseAdapter {
    private Context mContext;
    private List<Cell> cellsData;

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    private String orientation;

    public GridViewAdaptor(Context c, List<Cell> data) {
        mContext = c;
        cellsData = data;
    }

    public int getCount() {
        return 96;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            textView = new TextView(mContext);
            textView.setBackgroundColor(Color.LTGRAY);
            textView.setPadding(8, 8, 8, 8);
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        } else {
            textView = (TextView) convertView;
        }
        if(orientation.equals("portrait")){
            textView.setLayoutParams(new GridView.LayoutParams(130, 128));
        } else {
            textView.setLayoutParams(new GridView.LayoutParams(220, 120));
        }

        String cellValue = cellsData.get(position).getValue();
            textView.setText(cellValue);
            if(cellValue!= null && !cellValue.equals("")){
                int colorValue = colorForCell(cellValue);
                textView.setTextColor(colorValue);
            }

        return textView;
    }

    private int colorForCell(String cellValue) {
        if(cellValue.equals("M")){
            return Color.RED;
        }
        int value = Integer.parseInt(cellValue);
        switch (value){
            case 0:
                return Color.BLUE;
            case 1:
                return Color.RED;
            case 2:
                return Color.BLACK;
            case 3:
                return Color.MAGENTA;
            case 4:
                return Color.CYAN;
            case 5:
                return Color.YELLOW;
            case 6:
                return Color.DKGRAY;
            case 7:
                return Color.GRAY;
        }
        return Color.RED;
    }

}
