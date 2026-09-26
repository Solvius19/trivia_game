package org.example.view;

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

        return input.nextInt();
    }

    private static AttackType resolveAttack(int choice) {
        return switch (choice) {
            case 1 -> AttackType.BLOCK_OUT;
            case 2 -> AttackType.SCRAMBLE;
            case 3 -> AttackType.SKIP_TURN;
            case 4 -> AttackType.SELECT_NEXT_QUESTION;
            case 5 -> AttackType.TAX;
            case 6 -> AttackType.SWAP_SCORES;
            default -> null;
        };
    }


    public static void askQuestion(Question question, Player player, String attack) {
        String modifiedQuestion = Attack.modify(question.getQuestion(), attack);
        System.out.print("Your answer: ");
        String userAnswer = input.nextLine();

        boolean correct = TriviaController.checkAnswer(userAnswer, question);

        if (correct){
            System.out.println("Correct!");
        }
        else {
            System.out.println("Incorrect! The correct answer was: " + question.getAnswer());
        }
    }

    public static int[] pickQuestion(){
            System.out.print("Enter row (1-5) and column (1-6) of the question you want to answer (e.g., 2 3): ");
            int row = input.nextInt() - 1;
            int col = input.nextInt() - 1;
            input.nextLine();

            return new int[]{row, col};
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

}
