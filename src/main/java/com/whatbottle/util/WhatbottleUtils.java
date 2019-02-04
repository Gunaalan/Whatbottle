package com.whatbottle.util;

import com.whatbottle.data.Requests.MessageRequest;
import com.whatbottle.data.models.ReplyMessageRequest;

import java.io.UnsupportedEncodingException;
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

    @Override
    public OrderCallbackResponse callbackPartnerForOrder(String partnerId, String jsonBody) {
        OrderCallbackResponse orderCallbackResponse = null;
        CallbackUrl url = getCallbackUrl(partnerId, CallbackType.ORDER);
        if (url == null) {
            if (url == null) {
                log.info("order Callback url is not defined for partner" + partnerId);
                return orderCallbackResponse;
            }
        }
        log.info("json to post" + jsonBody);
        log.info("call back url is " + url.getUrl());
        HttpPost request = HttpMethodUtil.getPostRequest(url.getUrl());
        HttpRequester requester = HttpRequester.getHttpRequester();
        try {
            request.setEntity(new StringEntity(jsonBody));
            try (CloseableHttpResponse response = requester.getHttp().send(
                    request)) {
                int statusCode = response.getStatusLine().getStatusCode();
                String message = response.getStatusLine().getReasonPhrase();
                orderCallbackResponse = new OrderCallbackResponse();
                orderCallbackResponse.setStatus(statusCode);
                orderCallbackResponse.setMessage(message);
                log.info("requestJson ABHI" + jsonBody + "responseJson ABHI" + orderCallbackResponse.toString());
                log.info("callback for order done, for " + partnerId + " statusCode - " + statusCode + " message - " + message);

            } catch (Exception e) {
                log.error("unable to callback for order to partner "
                        + partnerId);
            }
        } catch (UnsupportedEncodingException e) {
            log.error("Error in parsing order call back response for partner "
                    + partnerId + " respnse : " + jsonBody);
        }
        return orderCallbackResponse;
    }

}