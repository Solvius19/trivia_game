package org.example;

public class Main {
    public static void main (String[] args){
        EventHelper.setupGame();
        OutputUtil.clear();
        System.out.println("Welcome to the Game!");
//        while(true) {
////            player = EventHelper.onMenu();
////            if (player == null) {
////                break;
////            }
//            OutputUtil.clear();
//        }
        EventHelper.printBoard();
        EventHelper.askQuestion(1, 1, new Player("Player 1"));
    }
}
