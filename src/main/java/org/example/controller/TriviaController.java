package org.example.controller;

import org.example.model.*;
import org.example.service.Attack;
import org.example.view.*;

import java.util.List;

public class TriviaController {

    public TriviaController() {
    }

    private static Game game;

    public static void setupGame() {
        OutputUtil.clear();
        Board board = new Board();
        OutputUtil.clear();
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

        Game.PendingAttack pending = consumePendingAttack(player);

        if (isSkipped(pending)) {
            System.out.println(player.getName() + "'s turn was skipped!");
            OutputUtil.enterToClear();
            return;
        }

        System.out.println(player.getName() + "'s turn!");
        System.out.println("Current score: " + player.getCurrentScore());

        TriviaView.printBoard(game.getBoard());

        Question currentQuestion = resolveQuestion(player, pending);
        AttackType textEffect = resolveTextEffect(pending);

        boolean correct = TriviaView.askQuestion(currentQuestion, player, textEffect);
        applyScoring(player, currentQuestion, correct);

        if (isTax(pending)) {
            Attack.tax(correct, pending.attacker(), player, currentQuestion.getValue());
        }

        game.getBoard().removeQuestion(currentQuestion);
        OutputUtil.enterToClear();
        offerAttackPurchase(player);
        OutputUtil.enterToClear();
    }

    private static Game.PendingAttack consumePendingAttack(Player player) {
        Game.PendingAttack pending = game.getPendingAttackFor(player);
        game.clearPendingAttackFor(player);
        return pending;
    }

    private static boolean isSkipped(Game.PendingAttack pending) {
        return pending != null && pending.type() == AttackType.SKIP;
    }

    private static boolean isTax(Game.PendingAttack pending) {
        return pending != null && pending.type() == AttackType.TAX;
    }

    private static Question resolveQuestion(Player player, Game.PendingAttack pending) {
        if (pending != null && pending.type() == AttackType.SELECT_NEXT) {
            System.out.println(player.getName() + "'s question was chosen for them!");
            return pending.selectedQuestion();
        }
        int[] location = TriviaView.pickQuestion();
        return game.getBoard().getBoard(location[0], location[1]);
    }

    private static AttackType resolveTextEffect(Game.PendingAttack pending) {
        if (pending != null && (pending.type() == AttackType.BLOCK_OUT || pending.type() == AttackType.SCRAMBLE)) {
            return pending.type();
        }
        return null;
    }

    private static void applyScoring(Player player, Question currentQuestion, boolean correct) {
        if (correct) {
            player.addScore(currentQuestion.getValue());
            player.incrementStreak();
        } else {
            player.subtractScore(currentQuestion.getValue());
            player.resetStreak();
        }
    }

    private static void offerAttackPurchase(Player buyer) {
        int choice = TriviaView.attackMenu(buyer);
        AttackType attack = resolveAttack(choice);

        if (attack == AttackType.NONE) {
            return;
        }

        if (buyer.getCurrentScore() < attack.getCost()) {
            System.out.println("Not enough points for that attack.");
            return;
        }

        buyer.subtractScore(attack.getCost());

        if (attack == AttackType.SWAP) {
            Player target = TriviaView.selectPlayer(game.getPlayers(), buyer);
            Attack.swapScores(buyer, target);
            return;
        }

        Player target = game.getPlayerAhead(buyer, 1);

        if (attack == AttackType.SELECT_NEXT) {
            TriviaView.printBoard(game.getBoard());
            int[] location = TriviaView.pickQuestion();
            Question chosen = game.getBoard().getBoard(location[0], location[1]);
            game.queueAttack(attack, buyer, target, chosen);
        } else {
            game.queueAttack(attack, buyer, target, null);
        }
    }

    private static AttackType resolveAttack(int choice) {
        return switch (choice) {
            case 1 -> AttackType.BLOCK_OUT;
            case 2 -> AttackType.SCRAMBLE;
            case 3 -> AttackType.SKIP;
            case 4 -> AttackType.SELECT_NEXT;
            case 5 -> AttackType.TAX;
            case 6 -> AttackType.SWAP;
            default -> AttackType.NONE;
        };
    }
}