package org.example;

public class Main {
    public static void main (String[] args){
        EventHelper.setupGame();
        OutputUtil.clear();
        EventHelper.printBoard();
        EventHelper.askQuestion(1, 1, new Player("Player 1"));
    }
}
