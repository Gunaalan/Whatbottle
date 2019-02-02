package com.whatbottle.data;

import com.whatbottle.util.Constants;
import org.apache.commons.lang3.StringUtils;

public enum Questions {
    INITIATE(Constants.initiateConvo),
    POST_QUESTION(Constants.questionPost),
    SATISFIED(Constants.questionAnswerSatisfied),
    MENU(String.join(",",Constants.menu)),
    REITERATE(Constants.iterateQuestion),
    QUESTION(Constants.questionMessage);

    Questions(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    private String question;
}
