package org.example;

import org.example.controller.TriviaController;
import org.example.view.OutputUtil;

public class Main {
    public static void main (String[] args){
        TriviaController.setupGame();
        OutputUtil.clear();
        TriviaController.runGame();
    }
}
