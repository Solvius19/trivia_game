package org.example.model;

import org.example.controller.TriviaController;
import org.example.view.OutputUtil;
import org.example.view.TriviaView;

import java.util.List;
import java.util.Scanner;

public class Attack {
    private Attack() {
        /* This utility class should not be instantiated */
    }


    public static String blockOut(String question){
        int length = question.length() / 6;
        if (length == 0) {
            length = 1;
        }

        for (int i = 0; i < length; i++) {
            int random = (int) (Math.random() * question.length());
            question = question.substring(0, random) + "#" + question.substring(random + 1);
        }
        return question;
    }

    public static void tax(boolean correct, Player player, Player player2, int value){
        int taxAmount = (int) (value * 0.1);
        if (correct) {
            player.subtractScore(taxAmount);
            player2.addScore(taxAmount);
            System.out.println(player.getName() + " was taxed " + taxAmount + "by " + player2.getName() + " points for answering correctly!");
        } else {
            player.addScore(taxAmount);
            player2.subtractScore(taxAmount);
            System.out.println(player.getName() + " was rewarded " + taxAmount + "by " + player2.getName() + " points for answering incorrectly!");
        }
    }
    public static Question blinded(Scanner input){
        System.out.println("\nSelect a question for the next player:");
        TriviaView.printBoard(TriviaController.getBoard());
        System.out.print("Enter row (1-6) and column (1-5) of the question (e.g., 2 3): ");
        int row = input.nextInt() - 1;
        int col = input.nextInt() - 1;
        input.nextLine();

        if (row < 0 || row >= 6 || col < 0 || col >= 5 || TriviaController.isEmpty(row, col)) {
            System.out.println("Invalid choice. No question selected.");
            return null;
        }
        System.out.println("You selected Row " + (row + 1) + ", Column " + (col + 1) + " for the next player!");
        OutputUtil.clear();
        return TriviaController.getBoard(row, col);
    }

    public static String scramble(String question){
        String[] words = question.split(" ");
        StringBuilder scrambledQuestion = new StringBuilder();

        for (String word : words) {
            if (word.length() > 3) {
                char[] letters = word.toCharArray();
                for (int i = 0; i < letters.length; i++) {
                    int randomIndex = (int) (Math.random() * letters.length);
                    char temp = letters[i];
                    letters[i] = letters[randomIndex];
                    letters[randomIndex] = temp;
                }
                scrambledQuestion.append(new String(letters)).append(" ");
            } else {
                scrambledQuestion.append(word).append(" ");
            }
        }
        return scrambledQuestion.toString().trim();
    }

    public static String modify(String question, String attack) {
        if (attack == null) {
            return question;
        }
        return switch (attack) {
            case "blockout" -> blockOut(question);
            case "scramble" -> scramble(question);
            default -> question;
        };
    }

    public static void swapScores(Scanner input, List<Player> players, Player currentPlayer) {
        while (true) {
            System.out.println("Choose a player to swap scores with:");
            int num = 1;
            for (Player p : players) {
                if (!p.equals(currentPlayer)) {
                    System.out.println(num + ". " + p.getName() + ": " + p.getCurrentScore());
                    num++;
                }
            }
            System.out.print("Enter your choice: ");
            int choice = input.nextInt();
            if (choice < 1 || choice >= num) {
                System.out.println("Invalid choice. Please choose again.");
                continue;
            }
            Player selectedPlayer = players.get(choice - 1);
            if (selectedPlayer.equals(currentPlayer)) {
                System.out.println("You cannot swap scores with yourself. Please choose again.");
                continue;
            }
            int tempScore = currentPlayer.getCurrentScore();
            currentPlayer.setScore(selectedPlayer.getCurrentScore());
            selectedPlayer.setScore(tempScore);
            break;
        }
    }

}
