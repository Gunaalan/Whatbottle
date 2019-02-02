package com.whatbottle.service.Impl;


import com.whatbottle.data.Requests.MessageRequest;
import com.whatbottle.data.models.TopicMessageRequest;
import com.whatbottle.data.pojos.Questions;
import com.whatbottle.repository.TopicMessageRequestRepository;
import com.whatbottle.service.Whatbottleservice;
import com.whatbottle.util.Constants;
import com.whatbottle.util.PostMesageToLIA;
import com.whatbottle.util.WhatbottleHelper;
import com.whatbottle.util.WhatbottleUtils;
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

    @Autowired
    WhatbottleHelper whatbottleHelper;

    @Autowired
    TopicMessageRequestRepository topicMessageRequestRepository;

    @Autowired
    PostMesageToLIA postAMessageToLia;

    private boolean chatEnabled = false;
    private Questions currentQuestion;


    @Override
    public ApiKeyAuth generateToken() throws Exception {
        return whatbottleHelper.generateJWTToken();
    }

    @Override

    public MessageResponse postAMessage(MessageRequest messageRequest, String userId) throws Exception {
        if(!chatEnabled){
            pushMessageToMongo(messageRequest, userId);
            return successfullyQueuedMessageResponse();
        } else {
            MessagePost messagePost = constructTextMessage(messageRequest.getMessage());
            return whatbottleHelper.postAMessage(messagePost, userId);
        }
    }

    private MessageResponse successfullyQueuedMessageResponse() {
        Message message = new Message();
        message.setText("successfully queued your message. It will be delivered soon");
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage(message);
        return messageResponse;
    }

    @Override
    public MessageResponse readAMessage(List<Message> messages) throws Exception {
        String userId = messages.get(0).getAuthorId();
        String name = messages.get(0).getName();
        if (messages.get(0).getText().equalsIgnoreCase(Constants.initiateConvo) && !chatEnabled) {
            chatEnabled = true;
            greetUser(name, userId);
            return postMenu(userId);
        } else
            return processIncomingMessage(messages.get(0), userId);
    }

    private void greetUser(String name,String userId) throws Exception {
        postAMessage(new MessageRequest(String.format(Constants.greetHello,name)),userId);
    }

    private MessageResponse processIncomingMessage(Message message, String userId) throws Exception {
        String text = message.getText();
        switch (currentQuestion) {
            case MENU:
                processMenu(text, userId);
                break;
            case QUESTION:
                fetchAnswer(text,userId);
                break;
            case SATISFIED:
                processSatiesfied(text, userId);
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
        if(response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("y"))
            reiterteMenu(userId);
        else if(response.equalsIgnoreCase("no") || response.equalsIgnoreCase("n"))
            askToPost(userId);
        else
            postInvalid(userId);
    }

    private void  reiterteMenu(String userId) throws Exception {
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

    private void   processUnsatisfied(String response, String userId) throws Exception{
        if(response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("Y")) {
            //post to community
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
        MessageResponse messageResponse = postAMessage(messageRequest,userId);
        currentQuestion=Questions.MENU;
        return messageResponse;
    }

    //Hack
    private void fetchAnswer(String question,String userId) throws Exception {
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

    private TopicMessageRequest pushMessageToMongo(MessageRequest messageRequest, String userId) {
      return  topicMessageRequestRepository.save(WhatbottleUtils.builder(messageRequest));
    }

    private void processMongoMessages() {
        List<TopicMessageRequest> topicMessageRequestsAll = topicMessageRequestRepository.findAll();
        for(TopicMessageRequest topicMessageRequest : topicMessageRequestsAll) {
            postAMessageToLia.postAMessageToCommunity(topicMessageRequest);
        }
    }

    private void askQuestion(String userId) throws Exception {
        postAMessage(new MessageRequest(Constants.questionMessageQuestion),userId);
        currentQuestion = Questions.QUESTION;
    }


}
