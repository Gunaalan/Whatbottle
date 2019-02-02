package com.whatbottle.service.Impl;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.whatbottle.data.Requests.MessageRequest;
import com.whatbottle.service.Whatbottleservice;
import com.whatbottle.util.WhatbottleHelper;
import io.smooch.client.auth.ApiKeyAuth;
import io.smooch.client.model.Enums;
import io.smooch.client.model.Message;
import io.smooch.client.model.MessagePost;
import io.smooch.client.model.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;

@Service
public class WhatbottleserviceImpl implements Whatbottleservice {

    @Autowired
    WhatbottleHelper whatbottleHelper;

    @Override
    public ApiKeyAuth generateToken() throws Exception {
        return whatbottleHelper.generateJWTToken();
    }

    @Override
    public MessageResponse postAMessage(MessageRequest messageRequest, String userId) throws Exception {
        MessagePost messagePost = constructTextMessage(messageRequest.getMessage());
        return whatbottleHelper.postAMessage(messagePost, userId);
    }

    public MessageResponse readAMessage(List<Message> messages) throws Exception{
        
    }



    private MessagePost constructTextMessage(String message) throws Exception {
        MessagePost messagePost = new MessagePost();
        messagePost.setRole("appMaker");
        messagePost.setType(Enums.MessageTypeEnum.TEXT.toString());
        messagePost.setText(message);
        return messagePost;
    }



}
