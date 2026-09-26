package org.example.service;

import org.example.model.AttackType;
import org.example.model.Player;
import org.example.model.Question;

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

    public static String modify(String questionText, AttackType attack) {
        if (attack == null) {
            return questionText;
        }
        return switch (attack) {
            case BLOCK_OUT -> blockOut(questionText);
            case SCRAMBLE -> scramble(questionText);
            default -> questionText;
        };
    }

    public static void swapScores(Player acting, Player target) {
        int temp = acting.getCurrentScore();
        acting.setScore(target.getCurrentScore());
        target.setScore(temp);
    }

    public static boolean isTax(AttackType attack) {
        return attack == AttackType.TAX;
    }

}
