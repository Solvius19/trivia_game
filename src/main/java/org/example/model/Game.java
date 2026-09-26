package org.example.model;

import java.util.List;

public class Game {

    private final List<Player> players;
    private final Board board;
    private int currentPlayerIndex;
    private PendingAttack pendingAttack;

    public record PendingAttack(AttackType type, Player attacker, Player target, Question selectedQuestion) {}

    public Game(List<Player> players, Board board) {
        this.players = players;
        this.board = board;
        this.currentPlayerIndex = 0;
        this.pendingAttack = null;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void advanceTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public Player getPlayerAhead(Player from, int turns) {
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

    public void queueAttack(AttackType type, Player attacker, Player target, Question selectedQuestion) {
        this.pendingAttack = new PendingAttack(type, attacker, target, selectedQuestion);
    }

    public PendingAttack getPendingAttackFor(Player player) {
        if (pendingAttack != null && pendingAttack.target() == player) {
            return pendingAttack;
        }
        return null;
    }

    public void clearPendingAttackFor(Player player) {
        if (pendingAttack != null && pendingAttack.target() == player) {
            pendingAttack = null;
        }
    }
}