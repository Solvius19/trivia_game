package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    public Question(String s, String s1, String cat, String easy, int i) {
        this.question = s;
        this.answer = s1;
        this.category = cat;
        this.difficulty = easy;
        this.value = i;
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
        return category;
    }


    public String getQuestion() {
        return question;
    }


    public String getAnswer() {
        return answer;
    }


    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
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
}
