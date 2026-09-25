package org.example.controller;

import org.example.model.*;
import org.example.service.BoardBuildEngine;
import org.example.view.*;

import java.util.*;

public class TriviaController {
    private TriviaController() {
        /* This utility class should not be instantiated */
    }

    private static final Scanner input = new Scanner(System.in);
    private static ArrayList<Player> players;

    public static void setupGame() {
        board = BoardBuildEngine.buildBoard();
        setupValues();
        OutputUtil.clear();
        players = TriviaView.generatePlayers();
    }

    private static void setupValues() {
        for (int row = 0; row < board.length; row++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[row][j] != null) {
                    int value = (row + 1) * 200;
                    board[row][j].setValue(value);
                }
            }
        }
    }

    public static void runGame() {
        while (!isGameOver()) {
            String attack = null;
            for (Player player : players) {
                doTurn(player);
            }
        }
        System.out.println("Game Over!");
        for (Player player : players) {
            System.out.println(player.getName() + ", Your final score is: " + player.getCurrentScore());
        }
        System.out.println("Thanks for playing!");
    }

    private static boolean isGameOver() {
        for (Question[] questions : board) {
            for (Question q : questions) {
                if (q != null) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void doTurn(Player player) {
        OutputUtil.clear();
        TriviaView.printBoard(board);
        Question currentQuestion = TriviaView.pickQuestion();
        int[] location = getQuestionLocation(currentQuestion);
        assert location != null;
        int row = location[0];
        int col = location[1];


        TriviaView.askQuestion(currentQuestion, player);
        board[row][col] = null;
        OutputUtil.enterToClear();
    }

    public static String getCategoryForColumn(int col) {
        if (board == null) return "Category";
        for (Question[] row : board) {
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

    private static Player getPlayer(Player player, int turns) {
        int currentIndex = players.indexOf(player);
        if (turns == 0 || player == null) {
            return player;
        }
        else {
            int nextIndex = (currentIndex + turns) % players.size();
            return players.get(nextIndex);
        }
    }


    private static boolean isTax(String attack){
        return attack != null && attack.equals("tax");
    }

    public static String getQuestion(Question question){
        StringBuilder output = new StringBuilder();
        output.append("Category: ").append(question.getCategory()).append("\n");
        output.append("Question: ").append(Attack.modify(question.getQuestion(), attack)).append("\n");
        Map<String, String> answerMap = question.getAnswers();
        for (Map.Entry<String, String> entry : answerMap.entrySet()) {
            output.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return output.toString();
    }

    public static void getFormattedBoard() {

    }
}
