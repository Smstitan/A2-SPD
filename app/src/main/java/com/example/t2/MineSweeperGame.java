package com.example.t2;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MineSweeperGame {
    private MineGrid mineGrid;
    private boolean gameOver;
    private boolean markMode;
    private boolean clearMode;
    private int markCount;
    private int numberBombs;
    private boolean timeExpired;

    public MineSweeperGame(int size, int numberBombs) {
        this.gameOver = false;
        this.markMode = false;
        this.clearMode = true;
        this.timeExpired = false;
        this.markCount = 0;
        this.numberBombs = numberBombs;
        mineGrid = new MineGrid(size);
        mineGrid.generateGrid(numberBombs);
    }

    public void handleCellClick(Cell cell) {
        if (!gameOver && !isGameWon() && !timeExpired && !cell.isRevealed()) {
            if (clearMode) {
                clear(cell);
            } else if (markMode) {
                mark(cell);
            }
        }
    }

    public void clear(Cell cell) {
        int index = getMineGrid().getCells().indexOf(cell);
        getMineGrid().getCells().get(index).setRevealed(true);

        if (cell.getValue() == Cell.BOMB) {
            gameOver = true;
        } else if (cell.getValue() == Cell.BLANK) {
            List<Cell> toClear = new ArrayList<>();
            List<Cell> toCheckAdjacents = new ArrayList<>();

            toCheckAdjacents.add(cell);

            while (toCheckAdjacents.size() > 0) {
                Cell c = toCheckAdjacents.get(0);
                int cellIndex = getMineGrid().getCells().indexOf(c);
                int[] cellPos = getMineGrid().toXY(cellIndex);
                for (Cell adjacent: getMineGrid().adjacentCells(cellPos[0], cellPos[1])) {
                    if (adjacent.getValue() == Cell.BLANK) {
                        if (!toClear.contains(adjacent)) {
                            if (!toCheckAdjacents.contains(adjacent)) {
                                toCheckAdjacents.add(adjacent);
                            }
                        }
                    } else {
                        if (!toClear.contains(adjacent)) {
                            toClear.add(adjacent);
                        }
                    }
                }
                toCheckAdjacents.remove(c);
                toClear.add(c);
            }

            for (Cell c: toClear) {
                c.setRevealed(true);
            }
        }
    }

    public void mark(Cell cell) {
        cell.setMarked(!cell.isMarked());
        int count = 0;
        for (Cell c: getMineGrid().getCells()) {
            if (c.isMarked()) {
                count++;
            }
        }
        markCount = count;
    }

    public boolean isGameWon() {
        int numbersUnrevealed = 0;
        for (Cell c: getMineGrid().getCells()) {
            if (c.getValue() != Cell.BOMB && c.getValue() != Cell.BLANK && !c.isRevealed()) {
                numbersUnrevealed++;
            }
        }

        if (numbersUnrevealed == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void toggleMode() {
        clearMode = !clearMode;
        markMode = !markMode;
    }

    public void outOfTime() {
        timeExpired = true;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public MineGrid getMineGrid() {
        return mineGrid;
    }

    public boolean isMarkMode() {
        return markMode;
    }

    public boolean isClearMode() {
        return clearMode;
    }

    public int getMarkCount() {
        return markCount;
    }

    public int getNumberBombs() {
        return numberBombs;
    }
}

