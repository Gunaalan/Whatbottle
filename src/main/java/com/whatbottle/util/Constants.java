package com.whatbottle.util;


import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Created by gunaas
 */
public interface Constants {

    String whatbottle = "*Whatbottle*\n";

    String initiateConvo = "Hey whatbottle";
    String start = whatbottle + "Please enter \" *Hey whatbottle*\" to initiate the conversation with Whatbottle!";
    String menu = whatbottle + "*1.* Ask a question\n*2.* Show Trending Topics\nEnter the option please:";

    String invalidMessage = whatbottle + "Oops! Looks like I didn't understand what you want me to do!";
    String helpMessage = whatbottle + "Glad to help You!";
    String postSuccessfulMessage = whatbottle + "Message Posted!";

    String questionMessageQuestion = whatbottle + "Please Ask your Question";
    String answerUnsatisfiedQuestion = whatbottle + "Sorry! I couldn't find the answer to this question, Would you like me to post this question on the community?\n Answer *Y* or *N*";
    String answerSatisfiedQuestion = whatbottle + "Are you Satisfied with the above answer?\n Answer *Y* or *N*";
    String iterateQuestion = whatbottle + "Is there anything else we can help you with?\n Answer *Y* or *N*";

    String greetHello = whatbottle + "Hello %s";

    String showTrendingTopics= "1. Iphone X\n2. Samsung Galaxy Note 9 heating issue\n3. pixel 3 photography";

    Map<String,String> questions = ImmutableMap.of("WHATS THE XIAOMI MI A2 COST IN BANGALORE?","13,670 INR",
            "WHICH CAMERAS ARE BEST FOR PHOTOGRAPHY?","Iphone Xs ,Pixel 3",
            "MOST TRENDING DISPLAYS OF ANY PHONE?","Samsung s9+, Iphone Xs", "S9+ NO SOUND WITH NOTIFICATIONS?" , "This worked:\n" +
                    "https://www.youtube.com/watch?v=H7fjg90LIsg");

}
