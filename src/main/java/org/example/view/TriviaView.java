package org.example.view;

import org.example.controller.TriviaController;
import org.example.model.Attack;
import org.example.model.Player;
import org.example.model.Question;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class TriviaView {

    private static final Scanner input = new Scanner(System.in);
    private static Question selectedQuestion = null;

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
    public static void printBoard(Question[][] boardState) {
        System.out.println();
        if (boardState == null || boardState.length == 0 || boardState[0] == null) {
            System.out.println("No board to display.");
            return;
        }

        int rows = boardState.length;
        int cols = boardState[0].length;

        final int colWidth = 20;

        System.out.print("  | ");
        String[] centeredCategories = new String[cols];
        for (int c = 0; c < cols; c++) {
            String category = TriviaController.getCategoryForColumn(c);
            String centered = centerString(category, colWidth);
            centeredCategories[c] = centered;
            System.out.print(color(c + 1, centered + " | "));
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
                Question q = boardState[r][c];
                if (q == null) {
                    System.out.print(centerString("", colWidth) + " | ");
                } else {
                    String value = "$" + q.getValue();
                    System.out.print(centerString(value, colWidth) + " | ");
                }
            }
            System.out.println();
        }
    }

    private static String padOrTrim(String s, int width) {
        if (s == null) s = "";
        if (s.length() > width) {
            if (width <= 3) return s.substring(0, width);
            return s.substring(0, width - 3) + "...";
        }
        return String.format("%-" + width + "s", s);
    }

    private static String centerString(String s, int width) {
        if (s == null) s = "";

        if (s.length() > width) {
            return padOrTrim(s, width);
        }

        if (s.length() == width) return s;

        int leftPadding = (width - s.length()) / 2;
        int rightPadding = width - s.length() - leftPadding;
        return " ".repeat(leftPadding) + s + " ".repeat(rightPadding);
    }

    private static void printAnswerChoices(Map<String, String> answerMap) {
        for (Map.Entry<String, String> entry : answerMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private static String color(int color, String param){
        return switch (color) {
            case 1 -> "\u001B[31m" + param + "\u001B[0m";
            case 2 -> "\u001B[32m" + param + "\u001B[0m";
            case 3 -> "\u001B[33m" + param + "\u001B[0m";
            case 4 -> "\u001B[34m" + param + "\u001B[0m";
            case 5 -> "\u001B[35m" + param + "\u001B[0m";
            case 6 -> "\u001B[36m" + param + "\u001B[0m";
            default -> "\u001B[0m";
        };
    }

    public static String attackMenu(Player player) {
        System.out.println(player.getName() + ", would you like to use an attack? (y/n)");
        System.out.println("Current score: " + player.getCurrentScore());
        String choice = input.nextLine();
        if (choice.equalsIgnoreCase("y")) {
            System.out.println("Choose an attack:");
            System.out.println("1. Block Out (-100 points)");
            System.out.println("2. Scramble (-200 points)");
            System.out.println("3. Skip Turn (-300 points)");
            System.out.println("4. Select Next Question (-500 points)");
            System.out.println("5. Tax (-700 points)");
            System.out.println("6. Swap Scores (-2000 points)");
            System.out.println("7. No attack");

            int attackChoice = input.nextInt();
            input.nextLine();
            switch (attackChoice) {
                case 1 -> {
                    if (player.getCurrentScore() < 100) {
                        System.out.println("Not enough points to use Block Out.");
                        return null;
                    }
                    player.subtractScore(100);
                    System.out.println("You chose Block Out.");
                    return "blockout";
                }
                case 2 -> {
                    if (player.getCurrentScore() < 200) {
                        System.out.println("Not enough points to use Scramble.");
                        return null;
                    }
                    player.subtractScore(200);
                    System.out.println("You chose Scramble.");
                    return "scramble";
                }
                case 3 -> {
                    if (player.getCurrentScore() < 300) {
                        System.out.println("Not enough points to use Skip Turn.");
                        return null;
                    }
                    player.subtractScore(300);
                    System.out.println("You chose Skip Turn.");
                    return "skip";
                }
                case 4 -> {
                    if (player.getCurrentScore() < 500) {
                        System.out.println("Not enough points to use Select Next Question.");
                        return null;
                    }
                    player.subtractScore(500);
                    System.out.println("You chose Select Next Question.");
                    selectedQuestion = Attack.blinded(input);
                    return "selectQuestion";
                }
                case 5 -> {
                    if (player.getCurrentScore() < 700) {
                        System.out.println("Not enough points to use Tax.");
                        return null;
                    }
                    player.subtractScore(700);
                    System.out.println("You chose Tax.");
                    return "tax";
                }
                case 6 -> {
                    if (player.getCurrentScore() < 2000) {
                        System.out.println("Not enough points to use Swap Scores.");
                        return null;
                    }
                    player.subtractScore(2000);
                    System.out.println("You chose Swap Scores.");
                    return "swap";
                }
                case 7 -> {
                    System.out.println("No attack used.");
                    return null;
                }
                default -> System.out.println("Invalid choice. No attack used.");
            }
        }
        OutputUtil.clear();
        return null;
    }

    public static void askQuestion(Question question, Player player, String attack) {
        System.out.println("Category: " + question.getCategory());
        System.out.println("Question: " + Attack.modify(question.getQuestion(), attack));
        Map<String, String> answerMap = question.getAnswers();
        printAnswerChoices(answerMap);

        System.out.print("Your answer: ");
        String userAnswer = input.nextLine();

        boolean correct = TriviaController.checkAnswer(player, userAnswer, question, answerMap);

        if (correct){
            System.out.println("Correct!");
        }
        else {
            System.out.println("Incorrect! The correct answer was: " + question.getAnswer());
        }
    }

    public static Question pickQuestion(){
        while (true) {
            if (selectedQuestion == null) {
                System.out.print("Enter row (1-5) and column (1-6) of the question you want to answer (e.g., 2 3): ");
                int row = input.nextInt() - 1;
                int col = input.nextInt() - 1;
                input.nextLine();

                if (TriviaController.isEmpty(row, col)) {
                    System.out.println("Invalid choice. Please choose again.");
                }
                else {
                    break;
                }
            }
            else {
                System.out.println("The previous player selected your question! Make sure to say thank you!");
                break;
            }
        }
        return selectedQuestion;
    }

}
