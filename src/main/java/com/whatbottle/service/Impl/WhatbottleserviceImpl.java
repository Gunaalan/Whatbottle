package com.whatbottle.service.Impl;

import com.whatbottle.data.Questions;
import com.whatbottle.data.Requests.MessageRequest;
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
import java.util.Objects;

@Service
@Slf4j
public class WhatbottleserviceImpl implements Whatbottleservice {

    private boolean chatEnabled = false;
    private long conversationStartTime;
    private Questions currentQuestion;
    private String question;

    @Autowired
    WhatbottleHelper whatbottleHelper;

    @Override
    public ApiKeyAuth generateToken() throws Exception {
        return whatbottleHelper.generateJWTToken();
    }

    @Override
    public MessageResponse postAMessage(MessageRequest messageRequest, String userId) throws Exception {
        if(!chatEnabled){
            pushMessageToMongo(messageRequest, userId);
            return new MessageResponse();   //need to change the return type of the function to string
        }
        else {
            MessagePost messagePost = constructTextMessage(messageRequest.getMessage());
            return whatbottleHelper.postAMessage(messagePost, userId);
        }
    }

    @Override
    public MessageResponse readAMessage(List<Message> messages) throws Exception {
        String userId = messages.get(0).getAuthorId();
        String name =messages.get(0).getName();
        if(messages.get(0).getText().equalsIgnoreCase(Constants.initiateConvo) && !chatEnabled){
            chatEnabled = true;
            greetUser(name,userId);
            return postMenu(userId); //only applicable for sandbox
        }
        else
            return processIncomingMessage(messages.get(0) , userId);
    }

    private void greetUser(String name,String userId) throws Exception {
        postAMessage(new MessageRequest(String.format(Constants.greetHello,name)),userId);
    }

    private MessageResponse processIncomingMessage(Message message ,String userId) throws Exception {
        String text = message.getText();
        switch (currentQuestion){
            case MENU:
                processMenu(text,userId);
                break;
            case QUESTION:
                fetchAnswer(text,userId);
                break;
            case SATISFIED:
                processSatiesfied(text,userId);
                break;
            case UNSATISFIED:
                processUnsatisfied(text,userId);
                break;
            case REITERATE:
                processReiterate(text,userId);
        }
        return new MessageResponse();
    }

    private void processSatiesfied(String response,String userId) throws Exception {
        if(response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("Y"))
            reiterteMenu(userId);
        else if(response.equalsIgnoreCase("no") || response.equalsIgnoreCase("n"))
            askToPost(userId);
        else
            postInvalid(userId);
    }

    private void reiterteMenu(String userId) throws Exception {
        postAMessage(new MessageRequest(Constants.iterateQuestion),userId);
        currentQuestion = Questions.REITERATE;
    }

    private void processReiterate(String response,String userId) throws Exception {
        if(response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("Y"))
            postMenu(userId);
        else if(response.equalsIgnoreCase("no") || response.equalsIgnoreCase("n"))
            terminateConversation(userId);
        else
            postInvalid(userId);
    }

    private void processUnsatisfied(String response, String userId) throws Exception{
        if(response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("Y")) {
            postAMessage(new MessageRequest(Constants.postSuccessfulMessage), userId);
            reiterteMenu(userId);
        }
        else if(response.equalsIgnoreCase("no") || response.equalsIgnoreCase("n"))
            reiterteMenu(userId);
        else
            postInvalid(userId);
    }

    private void askToPost(String userId) throws Exception {
        postAMessage(new MessageRequest(Constants.answerUnsatisfiedQuestion),userId);
        currentQuestion = Questions.UNSATISFIED;
    }

    private void terminateConversation(String userId) throws Exception {
        postAMessage(new MessageRequest(Constants.helpMessage),userId);
        chatEnabled = false;
        processMongoMessages();
    }

    private void postInvalid(String userId) throws Exception
    {
        postAMessage(new MessageRequest(Constants.invalidMessage),userId);
        reiterteMenu(userId);
    }

    private void processMenu(String response , String userId) throws Exception {
        switch (response){
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
        MessageResponse messageResponse = postAMessage(messageRequest,userId);
        currentQuestion=Questions.MENU;
        return messageResponse;
    }

    //Hack
    private void fetchAnswer(String question,String userId) throws Exception {
        this.question=question;
        if(Objects.isNull(Constants.questions.get(question.toUpperCase()))){
            askToPost(userId);
            currentQuestion = Questions.UNSATISFIED;
        }
        else
        {
            currentQuestion = Questions.SATISFIED;
            postAMessage(new MessageRequest(Constants.questions.get(question.toUpperCase())),userId);
            postAMessage(new MessageRequest(Constants.answerSatisfiedQuestion),userId);
        }
    }

    private void pushMessageToMongo(MessageRequest messageRequest, String userId){
        //code
    }

    private void processMongoMessages(){
        //code
    }

    private void askQuestion(String userId) throws Exception {
        postAMessage(new MessageRequest(Constants.questionMessageQuestion),userId);
        currentQuestion = Questions.QUESTION;
    }



}
