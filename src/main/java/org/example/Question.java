package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unused")
public class Question {

    private int value;
    private String type;
    private String difficulty;
    private String category;
    private String question;

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


    public String getAnswer() {
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
        if (value.contains("&#039;")) {
            value = value.replace("&#039;", "'");
        }
        if (value.contains("&quot;")) {
            value = value.replace("&quot;", "\"");
        }
        if (value.contains("&amp;")) {
            value = value.replace("&amp;", "&");
        }
        return value;
    }
}
