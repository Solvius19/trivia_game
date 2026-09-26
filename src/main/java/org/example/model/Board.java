package org.example.model;

import org.example.service.BoardBuildEngine;

public class Board {
    private static Question[][] boardState;

    static {
        boardState = BoardBuildEngine.buildBoard();
        setupValues();
    }

    public static Question[][] getBoard() {
        return boardState;
    }

    public static Question getBoard(int row, int col) {
        return boardState[row][col];
    }

    private static int[] getQuestionLocation(Question question){
        for (int row = 0; row < boardState.length; row++) {
            for (int col = 0; col < boardState[row].length; col++) {
                if (boardState[row][col] == question) {
                    return new int[]{row, col};
                }
            }
        }
        return null;
    }

    public static void removeQuestion(Question question) {
        int[] location = getQuestionLocation(question);
        if (location != null) {
            int row = location[0];
            int col = location[1];
            boardState[row][col] = null;
        }
    }

    public static boolean isEmpty(int row, int col){
        return boardState[row][col] == null;
    }


    public String getCategoryForColumn(int col) {
        if (boardState == null) return "Category";
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


    private static void setupValues() {
        for (int row = 0; row < boardState.length; row++) {
            for (int j = 0; j < boardState[0].length; j++) {
                if (boardState[row][j] != null) {
                    int value = (row + 1) * 200;
                    boardState[row][j].setValue(value);
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
