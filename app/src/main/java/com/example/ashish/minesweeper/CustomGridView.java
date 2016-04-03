package com.example.ashish.minesweeper;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomGridView extends AppCompatActivity {

    GridView textGridView;
    boolean recreated = false;
    int openCellsCount = 0;
    boolean isAlertshowing = false;


    List<Cell> cells = new ArrayList<>(96);
    List<List<Cell>> doubleDimensionalCells = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_grid_view);
        getSupportActionBar().hide();

        if(savedInstanceState == null || !savedInstanceState.containsKey("key") ) {
            createCells();
            placeBombs();
            adaptOneDimToTwoDim(cells);
        }
        else {
            cells = savedInstanceState.getParcelableArrayList("key");
            adaptOneDimToTwoDim(cells);

        }

        textGridView = (GridView) findViewById(R.id.gridView);
        final GridViewAdaptor adaptor = new GridViewAdaptor(this,cells);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            adaptor.setOrientation("portrait");
            textGridView.setNumColumns(8);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            adaptor.setOrientation("landscape");
            textGridView.setNumColumns(8);
        }
        textGridView.setHorizontalSpacing(1);
        textGridView.setVerticalSpacing(3);
        textGridView.setAdapter(adaptor);

        textGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                openCellsCount++;
                countNeighbourBombs(position);
                adaptor.notifyDataSetChanged();
            }
        });

    }

    public void createCells( ) {
        for(int i=0; i<96; i++){
            Cell newCell = new Cell("",false);
            cells.add(newCell);
        }
    }

    public void placeBombs(){
        Random randomGenerator = new Random();
        for (int idx = 1; idx <= 30; idx++){
            int randomInt = randomGenerator.nextInt(95);
            if(cells.get(randomInt).isBombPresent()){
                idx--;
                continue;
            }
            cells.get(randomInt).setIsBombPresent(true);
        }
    }

    public void adaptOneDimToTwoDim(List<Cell> cells){

            for(int i=0; i<12; i++){
                List<Cell> rowCells = new ArrayList<>();
                for(int j=0; j<8 ; j++){
                    rowCells.add(cells.get(i * 8 + j));
                }
                doubleDimensionalCells.add(rowCells);
            }
    }

    private void countNeighbourBombs(int index) {

        boolean isBomb = checkForTheBomb(index);
        if (isBomb){
            showDailog(index);
        }
        else {
            if(openCellsCount ==  66){
                showGameWonDailog();
            }
            Position twoDPosition = get2DPosition(index);
            List<Position> neighboringPositions = getNeighbors(twoDPosition);
            List<Cell> validCells = getValidCells(neighboringPositions);
            int numberOfBombs = calculateBombCounts(validCells);
            cells.get(index).setValue(Integer.toString(numberOfBombs));
        }

    }

    private void showDailog(int index){
        Toast.makeText(getApplicationContext(),
                "Mine clicked", Toast.LENGTH_SHORT).show();
         isAlertshowing = true;

        for(int i=0; i < cells.size() ; i++){
            Cell acell = cells.get(i);
            if(acell.isBombPresent){
                cells.get(i).setValue("M");
            }
        }

        new AlertDialog.Builder(this)
                .setTitle("Game Over!")
                .setMessage("You clicked on a mine. Do you want to restart?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                            recreated = true;
                        recreate();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                })
                .setCancelable(false)
                .show();
    }

    public void showGameWonDailog() {
         isAlertshowing = true;

        new AlertDialog.Builder(this)
                .setTitle("You Won!!")
                .setMessage("Wana Play Again?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        recreated = true;
                        recreate();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private boolean checkForTheBomb(int clickedposition){
        return cells.get(clickedposition).isBombPresent();

    }

    private int calculateBombCounts(List<Cell> validCells) {
        int totalBombCount = 0;
        for (Cell cell : validCells) {
            if (cell.isBombPresent()) {
                totalBombCount++;
            }
        }
        return totalBombCount;
    }

    private List<Cell> getValidCells(List<Position> neighboringPositions) {
        List<Cell> validCells = new ArrayList<>();
        for (Position position : neighboringPositions) {
            if(isValidPosition(position)) {
                List<Cell> cellRow = doubleDimensionalCells.get(position.getRow());
                Cell cell = cellRow.get(position.getColumn());
                validCells.add(cell);
            }
        }
        return validCells;
    }

    private boolean isValidPosition(Position position) {
        int row = position.getRow();
        int column = position.getColumn();
        int totalRows = doubleDimensionalCells.size();
        int totalColumns = doubleDimensionalCells.get(0).size();
        if ((row >=0 && row < totalRows)
                && (column >=0 && column < totalColumns)) {
            return true;
        }
        return false;
    }

    private List<Position> getNeighbors(Position twoDPosition) {
        int currentRow = twoDPosition.getRow();
        int currentColumn = twoDPosition.getColumn();

        List<Position> allNeighbours = new ArrayList<>();

        Position positionZero = new Position(currentRow-1, currentColumn);
        Position positionOne = new Position(currentRow+1, currentColumn);
        Position positionTwo = new Position(currentRow, currentColumn-1);
        Position positionThree = new Position(currentRow, currentColumn+1);
        Position positionFour = new Position(currentRow-1, currentColumn+1);
        Position positionFive = new Position(currentRow+1, currentColumn-1);
        Position positionSix = new Position(currentRow-1, currentColumn-1);
        Position positionSeven = new Position(currentRow+1, currentColumn+1);

        allNeighbours.add(positionZero);
        allNeighbours.add(positionOne);
        allNeighbours.add(positionTwo);
        allNeighbours.add(positionThree);
        allNeighbours.add(positionFour);
        allNeighbours.add(positionFive);
        allNeighbours.add(positionSix);
        allNeighbours.add(positionSeven);

        return allNeighbours;
    }

    private Position get2DPosition(int index) {
        Position position = null;
            int row = index/8;
            int column = index % 8;
             position = new Position(row, column);
        return position;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(recreated || isAlertshowing){
            outState = null;

        } else {
            outState.putParcelableArrayList("key", (ArrayList) cells);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void restartGame(View v){
        recreated = true;
        recreate();
    }

}