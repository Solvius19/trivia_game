package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.text.StringEscapeUtils;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unused")
public class Question {

    private int value;
    private String type;
    private String difficulty;
    private String category;
    private String question;
    private Map<String, String> answerMap;

    @JsonProperty("correct_answer")
    private String answer;

    @JsonProperty("incorrect_answers")
    private List<String> incorrectAnswers;

    public Question() {
    }

    public Question(String question, String answer, String category, String difficulty, int value) {
        this.question = question;
        this.answer = answer;
        this.category = category;
        this.difficulty = difficulty;
        this.value = value;
        this.answerMap = buildAnswersMap();
    }

    public int getValue() {
        return value;
    }


    public String getType() {
        return type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getCategory() {
        return formatValue(category);
    }


    public String getQuestion() {
        return formatValue(question);
    }
    public String getCorrectAnswer() {
        return formatValue(answer);
    }


    public List<String> getIncorrectAnswers() {
        List<String> formattedAnswers = new ArrayList<>();
        for (String s : incorrectAnswers) {
            s = formatValue(s);
            formattedAnswers.add(s);
        }
        return formattedAnswers;
    }

    public Map<String, String> buildAnswersMap() {
        List<String> answerChoices = new ArrayList<>(this.getIncorrectAnswers());
        answerChoices.add(answer);
        Collections.shuffle(answerChoices);
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < answerChoices.size(); i++) {
            String key = String.valueOf((char) ('A' + i));
            map.put(key, answerChoices.get(i));
        }
        return map;
    }

    public Map<String, String> getAnswerMap() {
        if (answerMap == null) {
            answerMap = buildAnswersMap();
        }
        return answerMap;
    }

    public boolean checkAnswer(String userAnswer) {
        try {
            return userAnswer.equalsIgnoreCase(getCorrectAnswer()) || answerMap.get(userAnswer.toUpperCase()).equalsIgnoreCase(getCorrectAnswer());
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Question{" +
                "value=" + value +
                ", type='" + type + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", category='" + category + '\'' +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", incorrectAnswers=" + incorrectAnswers +
                '}';
    }

    public void setValue(int i) {
        this.value = i;
    }

    private String formatValue(String value){
        return StringEscapeUtils.unescapeHtml4(value);
    }
}
