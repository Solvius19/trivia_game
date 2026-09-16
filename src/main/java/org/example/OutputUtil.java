package org.example;

import java.util.Scanner;

public class OutputUtil {

    public static void enterToClear() {
        Scanner input = new Scanner(System.in);
        IO.println("Enter to proceed...");
        input.nextLine();
        for (int i = 0; i < 50; i++) {
            IO.println();
        }
    }

    public static void clear() {
        for (int i = 0; i < 50; i++) {
            IO.println();
        }
    }

}
