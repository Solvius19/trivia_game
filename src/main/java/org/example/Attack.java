package org.example;

public class Attack {
    private Attack() {
        /* This utility class should not be instantiated */
    }


    public static String blockOut(String question){
        int hlength = question.length() / 6;
        if (hlength == 0) {
            hlength = 1;
        }

        for (int i = 0; i < hlength; i++) {
            int randomIndex = (int) (Math.random() * question.length());
            question = question.substring(0, randomIndex) + "#" + question.substring(randomIndex + 1);
        }
        return question;
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

    public static String modify(String question, String attack) {
        if (attack == null) {
            return question;
        }
        return switch (attack) {
            case "blockout" -> blockOut(question);
            case "scramble" -> scramble(question);
            default -> question;
        };
    }
}
