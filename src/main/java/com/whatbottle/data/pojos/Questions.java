package com.whatbottle.data.pojos;

import com.whatbottle.util.Constants;

public enum Questions {
    INITIATE(Constants.initiateConvo),
    UNSATISFIED(Constants.answerUnsatisfiedQuestion),
    SATISFIED(Constants.answerSatisfiedQuestion),
    MENU(String.join(",",Constants.menu)),
    REITERATE(Constants.iterateQuestion),
    QUESTION(Constants.questionMessageQuestion),
    START(Constants.start),
    MUTE(Constants.mute),
    UMUTE(Constants.unMute);

    Questions(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    private String question;
}
