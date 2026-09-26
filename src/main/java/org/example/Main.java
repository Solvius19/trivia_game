package org.example;

import org.example.controller.TriviaController;
import org.example.view.OutputUtil;

public class Main {
    public static void main (String[] args){
        System.out.println("Welcome to the Trivia Game!");
        TriviaController triviaController = new TriviaController();
        triviaController.setupGame();
        OutputUtil.clear();
        triviaController.runGame();
    }
}
