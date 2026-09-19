package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EventHelper {
    private EventHelper() {
        /* This utility class should not be instantiated */
    }


    private static Question[][] board = new Question[6][5];
    private static final Scanner input = new Scanner(System.in);
    private static final ArrayList<Player> players = new ArrayList<>();

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
        input.nextLine();
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

    public static void runGame() {
        while (!isGameOver()) {
            String attack = null;
            for (Player player : players) {
                doTurn(player, attack);
                if (players.size() > 1) {
                    attack = attackMenu(player);
                }
            }
        }
        System.out.println("Game Over!");
        for (Player player : players) {
            System.out.println(player.getName() + "'s final score: " + player.getCurrentScore());
        }
        System.out.println("Thanks for playing!");
    }

    private static String attackMenu(Player player) {
        System.out.println(player.getName() + ", would you like to use an attack? (y/n)");
        System.out.println("Current score: " + player.getCurrentScore());
        String choice = input.nextLine();
        if (choice.equalsIgnoreCase("y")) {
            System.out.println("Choose an attack:");
            System.out.println("1. Block Out (-100 points)");
            System.out.println("2. Scramble (-200 points)");
            int attackChoice = input.nextInt();
            input.nextLine(); // Consume newline
            if (attackChoice == 1) {
                if (player.getCurrentScore() < 100) {
                    System.out.println("Not enough points to use Block Out.");
                    return null;
                }
                player.subtractScore(100);
                System.out.println("You chose Block Out.");
                return "blockout";
            } else if (attackChoice == 2) {
                if (player.getCurrentScore() < 200) {
                    System.out.println("Not enough points to use Scramble.");
                    return null;
                }
                player.subtractScore(200);
                System.out.println("You chose Scramble.");
                return "scramble";
            } else {
                System.out.println("Invalid choice. No attack used.");
            }
        }
        OutputUtil.clear();
        return null;
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

    private static void doTurn(Player player, String attack) {
        System.out.println(player.getName() + "'s turn. Current score: " + player.getCurrentScore());
        printBoard();
        System.out.print("Enter row (1-6) and column (1-5) of the question you want to answer (e.g., 2 3): ");
        int row = input.nextInt() - 1;
        int col = input.nextInt() - 1;
        input.nextLine(); // Consume newline
        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length || board[row][col] == null) {
            System.out.println("Invalid choice. Please choose again.");
            return;
        }
        askQuestion(row, col, player, attack);
        OutputUtil.enterToClear();
    }

    public static void printBoard() {
        System.out.println();
        for (Question[] questions : board) {
            for (Question q : questions){
                if (q == null) {
                    System.out.print("     | ");
                } else {
                    System.out.print("$" + q.getValue() + " | ");
                }
            }
            System.out.println();
        }
    }

    public static void askQuestion(int row, int col, Player player, String attack) {
        Question question = board[row][col];
        System.out.println("Category: " + question.getCategory());
        System.out.println("Question: " + Attack.modify(question.getQuestion(), attack));
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
