package org.example;

import java.util.List;
import java.util.Scanner;

public class EventHelper {

    private static Question[][] board = new Question[6][5];
    private static Scanner input = new Scanner(System.in);

    public static void setupGame(){
        board = BoardBuildEngine.buildBoard();
        setupValues();
        Player player = new Player("Player 1");
    }

    private static void setupValues() {
        for (int row = 0; row < board.length; row++) {
            for (int j = 0; j < board[0].length; j++) {
                board[row][j].setValue((row + 1) * 100);
            }
        }
    }

    public static Player onMenu(Player player) {
        while (true) {
            System.out.print(player +
                    "\nChoice Menu:\n" +
                    "Enter choice: ");
            int choice = input.nextInt();
            if (choice == 0) {
                return null;
            } else {
                System.out.println("Invalid choice. Please choose again.");
            }
            OutputUtil.enterToClear();
        }
    }

    public static void printBoard() {
        System.out.println();
        for (Question[] questions : board) {
            for (Question q : questions){
                System.out.print("$" + q.getValue() + " | ");
            }
            System.out.println();
        }
    }


}
