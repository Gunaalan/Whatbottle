package com.whatbottle.util;

import com.whatbottle.data.Requests.MessageRequest;
import com.whatbottle.data.models.ReplyMessageRequest;

import java.util.Date;

/**
 * Created by guna on 30/06/18.
 */

public class WhatbottleUtils {

    public static ReplyMessageRequest MessageRequestToReplyMessageRequestConvertor(MessageRequest messageRequest) {
        ReplyMessageRequest replyMessageRequest = new ReplyMessageRequest();
        replyMessageRequest.setCreatedDate(new Date());
        replyMessageRequest.setMessage(messageRequest.getMessage());
        replyMessageRequest.setTopicName(messageRequest.getTopicName());
        replyMessageRequest.setUserName(messageRequest.getUserName());
        replyMessageRequest.setUserId(messageRequest.getUserId());
        return replyMessageRequest;
    }

    public static MessageRequest ReplyMessageRequestToMessageRequestConvertor(ReplyMessageRequest replyMessageRequest) {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setId(replyMessageRequest.getId());
        messageRequest.setCreatedDate(replyMessageRequest.getCreatedDate());
        messageRequest.setMessage(replyMessageRequest.getMessage());
        messageRequest.setTopicName(replyMessageRequest.getTopicName());
        messageRequest.setUserName(replyMessageRequest.getUserName());
        messageRequest.setUserId(replyMessageRequest.getUserId());
        return messageRequest;
    }

}