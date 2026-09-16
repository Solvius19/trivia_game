package org.example;

import java.util.List;
import java.util.Scanner;

public class EventHelper {

    private static Question[][] board = new Question[6][5];
    private static Scanner input = new Scanner(System.in);

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
        for (Question[] questions : board) {
            for (Question q : questions){
                System.out.print("$" + q.getValue() + " | ");
            }
            System.out.println();
        }
    }

//    public static void buildBoard(){
//        List<Question> questions = QuestionAPI.createQuestion();
//        for (int row = 0; row < board.length; row++){
//            for (int col = 0; col < board[row].length; col++){
//                board[row][col] = questions.get(row * board[row].length + col);
//            }
//        }
//    }


}
