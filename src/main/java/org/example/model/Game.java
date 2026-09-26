package org.example.model;

import java.util.List;

public class Game {

    private final List<Player> players;
    private final Board board;
    private int currentPlayerIndex;

    public Game(List<Player> players, Board board) {
        this.players = players;
        this.board = board;
        this.currentPlayerIndex = 0;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void advanceTurn(){
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public Player getNextPlayer(Player from, int turns) {
        int index = players.indexOf(from);
        int nextIndex = (index + turns) % players.size();
        return players.get(nextIndex);
    }

    public boolean isOver() {
        return board.isComplete();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Board getBoard() {
        return board;
    }

}
