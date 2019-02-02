package com.whatbottle.service.Impl;

import com.whatbottle.data.Requests.MessageRequest;
import com.whatbottle.data.pojos.Questions;
import com.whatbottle.repository.TopicMessageRequestRepository;
import com.whatbottle.service.Whatbottleservice;
import com.whatbottle.util.Constants;
import com.whatbottle.util.WhatbottleHelper;
import io.smooch.client.auth.ApiKeyAuth;
import io.smooch.client.model.Enums;
import io.smooch.client.model.Message;
import io.smooch.client.model.MessagePost;
import io.smooch.client.model.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class WhatbottleserviceImpl implements Whatbottleservice {

    @Autowired
    WhatbottleHelper whatbottleHelper;

//    @Autowired
//    TopicMessageRequestRepository topicMessageRequestRepository;

    private boolean chatEnabled = false;
    private long conversationStartTime;
    private Questions currentQuestion;
    private String question;


    @Override
    public ApiKeyAuth generateToken() throws Exception {
        return whatbottleHelper.generateJWTToken();
    }

    @Override
    public MessageResponse postAMessage(MessageRequest messageRequest, String userId, boolean isChatMessage) throws Exception {
        if (chatEnabled && !isChatMessage) {
            pushMessageToMongo(messageRequest, userId);
            return new MessageResponse();   //need to change the return type of the function to string
        } else {
            MessagePost messagePost = constructTextMessage(messageRequest.getMessage());
            return whatbottleHelper.postAMessage(messagePost, userId);
        }
    }

    @Override
    public MessageResponse readAMessage(List<Message> messages) throws Exception {
        String userId = messages.get(0).getAuthorId();
        String name = messages.get(0).getName();
        if (messages.get(0).getText().equalsIgnoreCase(Constants.initiateConvo) && !chatEnabled) {
            chatEnabled = true;
            greetUser(name, userId);
            return postMenu(userId); //only applicable for sandbox
        } else
            return processIncomingMessage(messages.get(0), userId);
    }

    private void greetUser(String name, String userId) throws Exception {
        postAMessage(new MessageRequest("Hello " + name), userId, true);
    }

    private MessageResponse processIncomingMessage(Message message, String userId) throws Exception {
        String text = message.getText();
        switch (currentQuestion) {
            case MENU:
                processMenu(text, userId);
                break;
            case SATISFIED:
                processSatiesfied(text, userId);
                break;
            case POST_QUESTION:
                processPostQuestion(text, userId);
                break;
            case QUESTION:
                fetchAnswer(text, userId);
                break;
        }
        return new MessageResponse();
    }

    private void processSatiesfied(String response, String userId) throws Exception {
        if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("Y"))
            terminateConversation(userId);
        else if (response.equalsIgnoreCase("no") || response.equalsIgnoreCase("n")) {
            askToPost(userId);
        } else
            postInvalid(userId);
    }

    private void processPostQuestion(String response, String userId) throws Exception {
        if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("Y"))
            postAMessage(new MessageRequest("Message Posted"), userId, true);
        else if (response.equalsIgnoreCase("no") || response.equalsIgnoreCase("n")) {
            terminateConversation(userId);
        } else
            postInvalid(userId);
    }

    private void askToPost(String userId) throws Exception {
        postAMessage(new MessageRequest(Constants.questionPost), userId, true);
        currentQuestion = Questions.POST_QUESTION;
    }

    private void terminateConversation(String userId) throws Exception {
        postAMessage(new MessageRequest("Glad to Help"), userId, true);
        chatEnabled = false;
        processMongoMessages();
    }

    private void postInvalid(String userId) throws Exception {
        postAMessage(new MessageRequest("Oops! Looks like I didn't understand what you want me to do!"), userId, true);
    }

    private void processMenu(String response, String userId) throws Exception {
        switch (response) {
            case "1":
                askQuestion(userId);
                break;
            case "2":
                break;
            default:
                postInvalid(userId);
        }
    }

    private MessagePost constructTextMessage(String message) throws Exception {
        MessagePost messagePost = new MessagePost();
        messagePost.setRole("appMaker");
        messagePost.setType(Enums.MessageTypeEnum.TEXT.toString());
        messagePost.setText(message);
        return messagePost;
    }

    private MessageResponse postMenu(String userId) throws Exception {
        MessageRequest messageRequest = new MessageRequest(Constants.menu);
        MessageResponse messageResponse = postAMessage(messageRequest, userId, true);
        currentQuestion = Questions.MENU;
        return messageResponse;
    }

    //Hack
    private void fetchAnswer(String question, String userId) throws Exception {
        this.question = question;
        currentQuestion = Questions.SATISFIED;
        postAMessage(new MessageRequest("Dummy Answer\n" + Constants.questionAnswerSatisfied), userId, true);
    }

    private void pushMessageToMongo(MessageRequest messageRequest, String userId) {
        //code
    }

    private void processMongoMessages() {
        //code
    }

    private void askQuestion(String userId) throws Exception {
        postAMessage(new MessageRequest(Constants.questionMessage), userId, true);
        currentQuestion = Questions.QUESTION;
    }


}
