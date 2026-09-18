package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EventHelper {

    private static Question[][] board = new Question[6][5];
    private static Scanner input = new Scanner(System.in);
    private static ArrayList<Player> players = new ArrayList<>();

    public static void setupGame(){
        board = BoardBuildEngine.buildBoard();
        setupValues();
        OutputUtil.clear();
        System.out.println("How many players? (1-4)");
        int choice = input.nextInt();
        if (choice < 1 || choice > 4) {
            System.out.println("Invalid choice. Please choose again.");
            return;
        }
        input.nextLine(); // Consume the newline character
        for (int i = 0; i < choice; i++) {
            System.out.print("Enter player " + (i + 1) + " name: ");
            String name = input.nextLine();
            players.add(new Player(name));
        }
    }

    private static void setupValues() {
        for (int row = 0; row < board.length; row++) {
            for (int j = 0; j < board[0].length; j++) {
                board[row][j].setValue((row + 1) * 100);
            }
        }
    }

//    public static Player onMenu() {
//        while (true) {
//            System.out.print(player +
//                    "\nChoice Menu:\n" +
//                    "Enter choice: ");
//            int choice = input.nextInt();
//            if (choice == 0) {
//                return null;
//            } else {
//                System.out.println("Invalid choice. Please choose again.");
//            }
//            OutputUtil.enterToClear();
//        }
//    }

    public static void printBoard() {
        System.out.println();
        for (Question[] questions : board) {
            for (Question q : questions){
                if (q == null) {
                    System.out.print("    | ");
                } else {
                    System.out.print("$" + q.getValue() + " | ");
                }
            }
            System.out.println();
        }
    }

    public static void askQuestion(int row, int col, Player player) {
        Question question = board[row][col];
        System.out.println("Category: " + question.getCategory());
        System.out.println("Question: " + question.getQuestion());
        System.out.print("Your answer: ");
        String userAnswer = input.nextLine();

        if (userAnswer.equalsIgnoreCase(question.getAnswer())) {
            System.out.println("Correct!");
            player.addScore(question.getValue());
        } else {
            System.out.println("Incorrect! The correct answer was: " + question.getAnswer());
            player.subtractScore(question.getValue());
        }
        board[row][col] = null; // Mark the question as answered
    }


    public static void gameOver(Player player) {
        System.out.println("Game Over! Your final score is: " + player.getCurrentScore());
    }
}
