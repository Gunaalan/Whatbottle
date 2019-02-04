package com.whatbottle.service.Impl;


import com.lithium.mineraloil.api.lia.api.v2.models.MessageV2Response;
import com.whatbottle.data.Requests.MessageRequest;
import com.whatbottle.data.Requests.WhatsAppMessage;
import com.whatbottle.data.models.ReplyMessageRequest;
import com.whatbottle.data.pojos.Questions;
import com.whatbottle.repository.ReplyMessageRequestRepository;
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
    ReplyMessageRequestRepository replyMessageRequestRepository;

    @Autowired
    PostMesageToLIA postAMessageToLia;

    private boolean chatEnabled = false;
    private Questions currentQuestion = Questions.START;


    @Override
    public ApiKeyAuth generateToken() throws Exception {
        return whatbottleHelper.generateJWTToken();
    }

    @Override
    public MessageResponse postRepliesMessage(MessageRequest messageRequest, String userId) throws Exception {
        if(chatEnabled){
            pushMessageToMongo(WhatbottleUtils.MessageRequestToReplyMessageRequestConvertor(messageRequest), userId);
            return successfullyQueuedMessageResponse();
        } else {
            MessagePost messagePost = constructReplyMessage(messageRequest);
            return whatbottleHelper.postAMessage(messagePost, userId);
        }
    }

    @Override
    public MessageResponse postWhatBottleMessage(MessageRequest messageRequest, String userId) throws Exception {
        if(chatEnabled){
            MessagePost messagePost = constructWhatbottleMessage(messageRequest);
            return whatbottleHelper.postAMessage(messagePost, userId);
        }
        else {
            throw new Exception("whatbottle-bot not enabled");
        }
    }

    private MessageResponse successfullyQueuedMessageResponse() {
        Message message = new Message();
        message.setText("successfully queued your message. It will be delivered soon");
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage(message);
        return messageResponse;
    }

    private MessageResponse successfullyRepliedMessageResponse() {
        Message message = new Message();
        message.setText("successfully replied in community.");
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage(message);
        return messageResponse;
    }

    @Override
    public MessageResponse readAMessage(List<Message> messages) throws Exception {
        String userId = messages.get(0).getAuthorId();
        String name = messages.get(0).getName();
        if (!chatEnabled) {
            if (messages.get(0).getText().equalsIgnoreCase(Constants.initiateConvo)) {
                chatEnabled = true;
                greetUser(name, userId);
                return postMenu(userId);
            } else {
                postReplyToCommunity(messages.get(0).getText());
                return successfullyRepliedMessageResponse();
            }
        } else {
            return processIncomingMessage(messages.get(0), userId);
        }
    }


    private void greetUser(String name,String userId) throws Exception {
        postWhatBottleMessage(new MessageRequest(String.format(Constants.greetHello,name)),userId);
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
            default:
                enterCorrectInitialization(userId);

        }
        return new MessageResponse();
    }

    private void postReplyToCommunity(String message) {
        System.out.println("to be done");
    }

    private void processSatiesfied(String response,String userId) throws Exception {
        if(response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("y"))
            reiterteMenu(userId);
        else if(response.equalsIgnoreCase("no") || response.equalsIgnoreCase("n"))
            askToPostInCommunity(userId);
        else
            postInvalid(userId);
    }

    private void  reiterteMenu(String userId) throws Exception {
        postWhatBottleMessage(new MessageRequest(Constants.iterateQuestion),userId);
        currentQuestion = Questions.REITERATE;
    }

    private void  enterCorrectInitialization(String userId) throws Exception {
        postWhatBottleMessage(new MessageRequest(Constants.start),userId);
        currentQuestion = Questions.START;
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
            postAMessageToLia.postToCommunityWithNewTopic(response);
            postWhatBottleMessage(new MessageRequest(Constants.postSuccessfulMessage), userId);
            reiterteMenu(userId);
        }
        else if(response.equalsIgnoreCase("no") || response.equalsIgnoreCase("n"))
            reiterteMenu(userId);
        else
            postInvalid(userId);
    }

    private void askToPostInCommunity(String userId) throws Exception {
        postWhatBottleMessage(new MessageRequest(Constants.answerUnsatisfiedQuestion),userId);
        currentQuestion = Questions.UNSATISFIED;
    }

    private void terminateConversation(String userId) throws Exception {
        postWhatBottleMessage(new MessageRequest(Constants.helpMessage),userId);
        chatEnabled = false;
        processMongoMessages();
    }

    private void postInvalid(String userId) throws Exception
    {
        postWhatBottleMessage(new MessageRequest(Constants.invalidMessage),userId);
        reiterteMenu(userId);
    }

    private void processMenu(String response, String userId) throws Exception {
        switch (response) {
            case "1":
                askQuestion(userId);
                break;
            case "2":
                showTendingTopics(userId);
                break;
            default:
                postInvalid(userId);
        }
    }

    private MessagePost constructReplyMessage(MessageRequest messageRequest) throws Exception {
        MessagePost messagePost = new MessagePost();
        messagePost.setRole("appMaker");
        messagePost.setType(Enums.MessageTypeEnum.TEXT.toString());
        messagePost.setText("*" + messageRequest.getTopicName() + "*\n" + "_" + messageRequest.getUserName() +"_"+ " : " + messageRequest.getMessage());
        return messagePost;
    }

    private MessagePost constructWhatbottleMessage(MessageRequest messageRequest) throws Exception {
        MessagePost messagePost = new MessagePost();
        messagePost.setRole("appMaker");
        messagePost.setType(Enums.MessageTypeEnum.TEXT.toString());
        messagePost.setText(messageRequest.getMessage());
        return messagePost;
    }

    private MessageResponse postMenu(String userId) throws Exception {
        MessageRequest messageRequest = new MessageRequest(Constants.menu);
        MessageResponse messageResponse = postWhatBottleMessage(messageRequest,userId);
        currentQuestion=Questions.MENU;
        return messageResponse;
    }

    private void showTendingTopics(String userId) throws  Exception {
        postWhatBottleMessage(new MessageRequest(Constants.showTrendingTopics), userId);
        reiterteMenu(userId);
    }
    //Hack
    private void fetchAnswer(String question,String userId) throws Exception {
        if(Objects.isNull(Constants.questions.get(question.toUpperCase()))){
            askToPostInCommunity(userId);
            currentQuestion = Questions.UNSATISFIED;
        }
        else
        {
            currentQuestion = Questions.SATISFIED;
            postWhatBottleMessage(new MessageRequest(Constants.questions.get(question.toUpperCase())),userId);
            postWhatBottleMessage(new MessageRequest(Constants.answerSatisfiedQuestion),userId);
        }
    }

    private ReplyMessageRequest pushMessageToMongo(ReplyMessageRequest replyMessageRequest, String userId) {
      return  replyMessageRequestRepository.save(replyMessageRequest);
    }

    private void processMongoMessages() throws  Exception {
        List<ReplyMessageRequest> replyMessageRequestsAll = replyMessageRequestRepository.findAll();
        for(ReplyMessageRequest replyMessageRequest : replyMessageRequestsAll) {
           postRepliesMessage(WhatbottleUtils.ReplyMessageRequestToMessageRequestConvertor(replyMessageRequest), replyMessageRequest.getUserId());
        }
    }

    private void askQuestion(String userId) throws Exception {
        postWhatBottleMessage(new MessageRequest(Constants.questionMessageQuestion),userId);
        currentQuestion = Questions.QUESTION;
    }

    @Override
    public MessageResponse replyToTopic(WhatsAppMessage whatsAppMessage) throws Exception {
        MessageV2Response response = postAMessageToLia.replyToTopic(whatsAppMessage);
        Message message = new Message();
        message.setText(response.toString());
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage(message);
        return messageResponse;
    }


}
