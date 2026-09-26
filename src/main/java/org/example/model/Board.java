package org.example.model;

import org.example.service.BoardBuildEngine;

public class Board {

    private final Question[][] boardState;

    public Board() {
        this.boardState = BoardBuildEngine.buildBoard();
        setupValues();
    }

    public Question[][] getBoard() {
        return boardState;
    }

    public Question getBoard(int row, int col) {
        return boardState[row][col];
    }

    private int[] getQuestionLocation(Question question) {
        for (int row = 0; row < boardState.length; row++) {
            for (int col = 0; col < boardState[row].length; col++) {
                if (boardState[row][col] == question) {
                    return new int[]{row, col};
                }
            }
        }
        return null;
    }

    public void removeQuestion(Question question) {
        int[] location = getQuestionLocation(question);
        if (location != null) {
            boardState[location[0]][location[1]] = null;
        }
    }

    public boolean isEmpty(int row, int col) {
        return boardState[row][col] == null;
    }

    public boolean isValid(int row, int col) {
        return row >= 0 && row < boardState.length
                && col >= 0 && col < boardState[row].length;
    }

    public String getCategoryForColumn(int col) {
        for (Question[] row : boardState) {
            if (row == null) continue;
            if (col >= 0 && col < row.length) {
                Question q = row[col];
                if (q != null && q.getCategory() != null && !q.getCategory().isBlank()) {
                    return q.getCategory();
                }
            }
        }
        return "Category";
    }

    private void setupValues() {
        for (int row = 0; row < boardState.length; row++) {
            for (int col = 0; col < boardState[row].length; col++) {
                if (boardState[row][col] != null) {
                    boardState[row][col].setValue((row + 1) * 200);
                }
            }
        }
    }

    public boolean isComplete() {
        for (Question[] questions : boardState) {
            for (Question q : questions) {
                if (q != null) {
                    return false;
                }
            }
        }
        return true;
    }
}