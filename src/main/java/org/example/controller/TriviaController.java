package org.example.controller;

import org.example.model.*;
import org.example.service.BoardBuildEngine;
import org.example.view.*;

import java.util.*;

public class TriviaController {
    public TriviaController() {
    }

    private static final Scanner input = new Scanner(System.in);
    private static Game game;

    public static void setupGame() {
        OutputUtil.clear()
        Board board = new Board();
        List<Player> players = TriviaView.generatePlayers();
        game = new Game(players, board);
    }

    public static void runGame() {
        while (!game.isOver()) {
            doTurn(game.getCurrentPlayer());
            game.advanceTurn();
        }
        System.out.println("Game Over!");
        for (Player player : game.getPlayers()) {
            System.out.println(player.getName() + ", Your final score is: " + player.getCurrentScore());
        }
        System.out.println("Thanks for playing!");
    }

    private static void doTurn(Player player) {
        OutputUtil.clear();
        TriviaView.printBoard(game.getBoard());
        int[] location = TriviaView.pickQuestion();
        int row = location[0];
        int col = location[1];

        Question currentQuestion = game.getBoard().getBoard(row, col);
        TriviaView.askQuestion(currentQuestion, player, /* ? */ null);
        game.getBoard().removeQuestion(currentQuestion);
        OutputUtil.enterToClear();
    }

    public static void getFormattedBoard() {

    }
}
