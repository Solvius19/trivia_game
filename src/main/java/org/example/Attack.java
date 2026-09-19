package org.example;

public class Attack {

    public static String blockOut(Player player, String question){
        if (player.getCurrentScore() < 100) {
            return question;
        }
        else {
            player.subtractScore(100);
        }
        int hlength = question.length() / 6;

        for (int i = 0; i < hlength; i++) {
            int randomIndex = (int) (Math.random() * question.length());
            question = question.substring(0, randomIndex) + "#" + question.substring(randomIndex + 1);
        }
        return question;
    }

    public static String scramble(Player player, String question){
        if (player.getCurrentScore() < 200) {
            return question;
        }
        else {
            player.subtractScore(200);
        }
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

}
