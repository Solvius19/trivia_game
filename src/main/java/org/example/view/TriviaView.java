package org.example.view;

import org.example.model.*;
import org.example.service.Attack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TriviaView {

    private static final Scanner input = new Scanner(System.in);

    public static ArrayList<Player> generatePlayers(){
        ArrayList<Player> players = new ArrayList<>();
        System.out.println("How many players? (1-4)");
        while (input.hasNext()) {
            if (input.hasNextInt()) {
                break;
            } else {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
                input.next();
            }
        }
        int choice = input.nextInt();
        if (choice < 1 || choice > 4) {
            System.out.println("Invalid choice. Please choose again.");
        }
        input.nextLine();
        for (int i = 0; i < choice; i++) {
            System.out.print("Enter player " + (i + 1) + " name: ");
            String name = input.nextLine();
            players.add(new Player(name));
        }
        return players;
    }
    public static void printBoard(Board board) {
        System.out.println();
        if (board == null) {
            System.out.println("No board to display.");
            return;
        }

        int rows = 5;
        int cols = 6;

        final int colWidth = 20;

        System.out.print("  | ");
        String[] centeredCategories = new String[cols];
        for (int c = 0; c < cols; c++) {
            String category = board.getCategoryForColumn(c);
            String centered = OutputUtil.centerString(category, colWidth);
            centeredCategories[c] = centered;
            System.out.print(OutputUtil.color(c + 1, centered + " | "));
        }
        System.out.println();

        System.out.print("  | ");
        for (int c = 0; c < cols; c++) {
            String centered = centeredCategories[c];
            String underline = "-".repeat(centered.length());
            System.out.print(underline + " | ");
        }
        System.out.println();


        for (int r = 0; r < rows; r++) {
            System.out.print(r + 1 + " | ");
            for (int c = 0; c < cols; c++) {
                Question q = board.getBoard(r, c);
                if (q == null) {
                    System.out.print(OutputUtil.centerString("", colWidth) + " | ");
                } else {
                    String value = "$" + q.getValue();
                    System.out.print(OutputUtil.centerString(value, colWidth) + " | ");
                }
            }
            System.out.println();
        }
    }

    public static int attackMenu(Player player) {
        System.out.println(player.getName() + ", would you like to use an attack? (y/n)");
        System.out.println("Current score: " + player.getCurrentScore());

        String choice = input.nextLine();
        if (!choice.equalsIgnoreCase("y")) {
            return 7;
        }

        System.out.println("Choose an attack:");
        System.out.println("1. Block Out (-100 points)");
        System.out.println("2. Scramble (-200 points)");
        System.out.println("3. Skip Turn (-300 points)");
        System.out.println("4. Select Next Question (-500 points)");
        System.out.println("5. Tax (-700 points)");
        System.out.println("6. Swap Scores (-2000 points)");
        System.out.println("7. No attack");

        int menuChoice = input.nextInt();
        input.nextLine();
        return menuChoice;
    }

    public static boolean askQuestion(Question question, Player player, AttackType attack) {
        String modifiedQuestion = Attack.modify(question.getQuestion(), attack);
        System.out.println("Category: " + question.getCategory());
        System.out.println("Question: " + modifiedQuestion);
        Map<String, String> answerMap = question.getAnswerMap();
        for (Map.Entry<String, String> entry : answerMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.print("Your answer: ");
        String userAnswer = input.nextLine();

        boolean correct = question.checkAnswer(userAnswer);

        if (correct) {
            System.out.println("Correct!");
        } else {
            System.out.println("Incorrect! The correct answer was: " + question.getCorrectAnswer());
        }
        return correct;
    }

    public static int[] pickQuestion(){
            System.out.print("Enter row (1-5) and column (1-6) of the question you want to answer (e.g., 2 3): ");
            int row = input.nextInt() - 1;
            int col = input.nextInt() - 1;
            input.nextLine();

            return new int[]{row, col};
    }

    public static Player selectPlayer(List<Player> players, Player buyer) {
        System.out.println("Select a player to interact with:");
        for (int i = 0; i < players.size(); i++) {
            System.out.println((i + 1) + ". " + players.get(i).getName());
        }
        System.out.print("Enter your choice (1-" + players.size() + "): ");
        int choice = input.nextInt();
        input.nextLine();
        return players.get(choice - 1);
    }
}
