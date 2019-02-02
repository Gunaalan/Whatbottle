package com.whatbottle.util;


import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Created by gunaas
 */
public interface Constants {

    String initiateConvo = "Hey Whatbottle";
    String menu = "1. Ask a question\n2. Show Trending Topics\nEnter the option #";

    String invalidMessage = "Oops! Looks like I didn't understand what you want me to do!";
    String helpMessage = "Glad to help You!";
    String postSuccessfulMessage = "Message Posted";

    String questionMessageQuestion = "Please Ask your Question";
    String answerUnsatisfiedQuestion = "Sorry! I couldn't find the answer to the question, Would you like to post the question on the community?\n Answer Y or N";
    String answerSatisfiedQuestion = "Are you Satisfied with the above answer?\n Answer Y or N";
    String iterateQuestion = "Is there anything else we can help you with?\n Answer Y or N";

    String greetHello = "Hello %s";

    Map<String,String> questions = ImmutableMap.of("IS SETUP DONE","YES",
            "IS THIS A GENUINE QUESTION","NO",
            "WHAT IS A CONSTRUCTOR","JAVA CONSTRUCTOR TUTORIAL WITH PROGRAM EXAMPLES. JAVA CONSTRUCTOR: A CONSTRUCTOR IN JAVA IS A METHOD WHICH IS USED USED TO INITIALIZE OBJECTS. CONSTRUCTOR METHOD OF A CLASS HAS THE SAME NAME AS THAT OF THE CLASS, THEY ARE CALLED OR INVOKED WHEN AN OBJECT OF A CLASS IS CREATED AND CAN'T BE CALLED EXPLICITLY.");
}
