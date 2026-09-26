package org.example.view;

import java.util.Scanner;

public class OutputUtil {

    public static void enterToClear() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter to proceed...");
        input.nextLine();
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    public static void clear() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }


    private static String padOrTrim(String s, int width) {
        if (s == null) s = "";
        if (s.length() > width) {
            if (width <= 3) return s.substring(0, width);
            return s.substring(0, width - 3) + "...";
        }
        return String.format("%-" + width + "s", s);
    }

    private static String centerString(String s, int width) {
        if (s == null) s = "";

        if (s.length() > width) {
            return padOrTrim(s, width);
        }

        if (s.length() == width) return s;

        int leftPadding = (width - s.length()) / 2;
        int rightPadding = width - s.length() - leftPadding;
        return " ".repeat(leftPadding) + s + " ".repeat(rightPadding);
    }

    private static String color(int color, String param){
        return switch (color) {
            case 1 -> "\u001B[31m" + param + "\u001B[0m";
            case 2 -> "\u001B[32m" + param + "\u001B[0m";
            case 3 -> "\u001B[33m" + param + "\u001B[0m";
            case 4 -> "\u001B[34m" + param + "\u001B[0m";
            case 5 -> "\u001B[35m" + param + "\u001B[0m";
            case 6 -> "\u001B[36m" + param + "\u001B[0m";
            default -> "\u001B[0m";
        };
    }


}
