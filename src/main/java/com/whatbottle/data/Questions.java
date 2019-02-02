package com.whatbottle.data;

import com.whatbottle.util.Constants;

public enum Questions {
    INITIATE(Constants.initiateConvo),
    UNSATISFIED(Constants.answerUnsatisfiedQuestion),
    SATISFIED(Constants.answerSatisfiedQuestion),
    MENU(String.join(",",Constants.menu)),
    REITERATE(Constants.iterateQuestion),
    QUESTION(Constants.questionMessageQuestion);

    Questions(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    private String question;
}
