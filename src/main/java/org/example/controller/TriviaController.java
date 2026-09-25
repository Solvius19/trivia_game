package org.example.controller;

import org.example.model.Attack;
import org.example.model.BoardBuildEngine;
import org.example.model.Player;
import org.example.model.Question;
import org.example.view.OutputUtil;
import org.example.view.TriviaView;

import java.util.*;

public class TriviaController {
    private TriviaController() {
        /* This utility class should not be instantiated */
    }

    private static Question[][] board = new Question[5][6];
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

    private static Player getPreviousPlayer(Player player) {
        int currentIndex = players.indexOf(player);
        int previousIndex = (currentIndex - 1 + players.size()) % players.size();
        return players.get(previousIndex);
    }

    public static boolean checkAnswer(Player player, String userAnswer, Question question, Map<String, String> answerMap) {
        try {
            boolean correct = isCorrect(userAnswer, question, answerMap);
            if (correct) {
                player.addScore(question.getValue());
                player.incrementStreak();
            } else {
                if (isTax(attack)) {
                    Attack.tax(false, player, getPreviousPlayer(player), question.getValue());
                } else {
                    player.subtractScore(question.getValue());
                }
                player.resetStreak();
            }
        } catch (NullPointerException e) {
            if (isTax(attack)) {
                Attack.tax(correct, player, getPreviousPlayer(player), question.getValue());
            } else {
                player.subtractScore(question.getValue());
            }
            player.resetStreak();
        }
        finally {
            board[row][col] = null;
        }
    }

    private static boolean isTax(String attack){
        return attack != null && attack.equals("tax");
    }

    private static boolean isCorrect(String userAnswer, Question question, Map<String, String> answerMap){
        return userAnswer.equalsIgnoreCase(question.getAnswer()) || answerMap.get(userAnswer.toUpperCase()).equalsIgnoreCase(question.getAnswer());
    }

    public static boolean isEmpty(int row, int col){
        return board[row][col] == null;
    }

    public static Question[][] getBoard() {
        return board;
    }

    public static Question getBoard(int row, int col) {
        return board[row][col];
    }

    private static int[] getQuestionLocation(Question question){
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] == question) {
                    return new int[]{row, col};
                }
            }
        }
        return null;
    }
}
