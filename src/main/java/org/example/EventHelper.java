package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class EventHelper {
    private EventHelper() {
        /* This utility class should not be instantiated */
    }


    private static Question[][] board = new Question[6][5];
    private static final Scanner input = new Scanner(System.in);
    private static final ArrayList<Player> players = new ArrayList<>();
    private static int[] selectedQuestion = null;

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
                if (attack != null && attack.equals("skip")) {
                    System.out.println(player.getName() + " has had their turn skipped!");
                    continue;
                }
                doTurn(player, attack);
                if (players.size() > 1) {
                    attack = attackMenu(player);
                    OutputUtil.clear();
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
            System.out.println("3. Select Next Question (-500 points)");
            System.out.println("4. Tax (-300 points)");
            System.out.println("5. Swap Scores (-400 points)");
            System.out.println("6. Skip Turn (-200 points)");
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
                    if (player.getCurrentScore() < 500) {
                        System.out.println("Not enough points to use Select Next Question.");
                        return null;
                    }
                    player.subtractScore(500);
                    System.out.println("You chose Select Next Question.");
                    selectedQuestion = Attack.blinded();
                    return "selectQuestion";
                }
                case 4 -> {
                    if (player.getCurrentScore() < 300) {
                        System.out.println("Not enough points to use Tax.");
                        return null;
                    }
                    player.subtractScore(300);
                    System.out.println("You chose Tax.");
                    return "tax";
                }
                case 5 -> {
                    if (player.getCurrentScore() < 400) {
                        System.out.println("Not enough points to use Swap Scores.");
                        return null;
                    }
                    player.subtractScore(400);
                    System.out.println("You chose Swap Scores.");
                    return "swap";
                }
                case 6 -> {
                    if (player.getCurrentScore() < 200) {
                        System.out.println("Not enough points to use Skip Turn.");
                        return null;
                    }
                    player.subtractScore(200);
                    System.out.println("You chose Skip Turn.");
                    return "skip";
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
        if (attack != null && attack.equals("swap")) {
            System.out.println(player.getName() + " has had their score swapped.");
            return;
        }
        System.out.println(player.getName() + "'s turn. Current score: " + player.getCurrentScore());
        printBoard();

        int row;
        int col;
        while (true) {
            if (selectedQuestion != null) {
                row = selectedQuestion[0];
                col = selectedQuestion[1];
                System.out.println("The previous player selected: Row " + (row + 1) + ", Column " + (col + 1));
                selectedQuestion = null;
            } else {
                System.out.print("Enter row (1-6) and column (1-5) of the question you want to answer (e.g., 2 3): ");
                row = input.nextInt() - 1;
                col = input.nextInt() - 1;
                input.nextLine();
            }

            if (row < 0 || row >= board.length || col < 0 || col >= board[0].length || board[row][col] == null) {
                System.out.println("Invalid choice. Please choose again.");
            } else {
                break;
            }
        }
        askQuestion(row, col, player, attack);
        OutputUtil.enterToClear();
    }

    public static String centerString(String s, int width) {
        if (s == null) s = "";

        if (s.length() > width) {
            return padOrTrim(s, width);
        }

        if (s.length() == width) return s;

        int leftPadding = (width - s.length()) / 2;
        int rightPadding = width - s.length() - leftPadding;
        return " ".repeat(leftPadding) + s + " ".repeat(rightPadding);
    }

    public static void printBoard() {
        System.out.println();
        if (board == null || board.length == 0 || board[0] == null) {
            System.out.println("No board to display.");
            return;
        }

        int rows = board.length;
        int cols = board[0].length;

        final int colWidth = 20;

        System.out.print("  | ");
        String[] centeredCategories = new String[cols];
        for (int c = 0; c < cols; c++) {
            String category = getCategoryForColumn(c);
            String centered = centerString(category, colWidth);
            centeredCategories[c] = centered;
            System.out.print(centered + " | ");
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
                Question q = board[r][c];
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


    private static String getCategoryForColumn(int col) {
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

    private static String padOrTrim(String s, int width) {
        if (s == null) s = "";
        if (s.length() > width) {
            if (width <= 3) return s.substring(0, width);
            return s.substring(0, width - 3) + "...";
        }
        return String.format("%-" + width + "s", s);
    }

    public static void askQuestion(int row, int col, Player player, String attack) {
        Question question = board[row][col];
        List<String> answerChoices = new ArrayList<>(question.getIncorrectAnswers());
        answerChoices.add(question.getAnswer());
        Collections.shuffle(answerChoices);
        System.out.println("Category: " + question.getCategory());
        System.out.println("Question: " + Attack.modify(question.getQuestion(), attack));
        System.out.println("Answer choices: " + answerChoices);

        System.out.print("Your answer: ");
        String userAnswer = input.nextLine();

        if (userAnswer.equalsIgnoreCase(question.getAnswer())) {
            System.out.println("Correct!");
            if (isTax(attack)) {
                Attack.tax(true, player, getPreviousPlayer(player), question.getValue());
            }
            else {
                player.addScore(question.getValue());
            }
        } else {
            System.out.println("Incorrect! The correct answer was: " + question.getAnswer());
            if (isTax(attack)) {
                Attack.tax(false, player, getPreviousPlayer(player), question.getValue());
            }
            else {
                player.subtractScore(question.getValue());
            }
        }
        board[row][col] = null;
    }

    private static Player getPreviousPlayer(Player player) {
        int currentIndex = players.indexOf(player);
        int previousIndex = (currentIndex - 1 + players.size()) % players.size();
        return players.get(previousIndex);
    }

    public static boolean isTax(String attack){
        return attack != null && attack.equals("tax");
    }



    public static void gameOver(Player player) {
        System.out.println("Game Over! Your final score is: " + player.getCurrentScore());
    }

    public static boolean isAnswered(int row, int col){
        return board[row][col] == null;
    }
}
