package com.whatbottle.controller;

import com.whatbottle.data.Requests.MessageRequest;
import com.whatbottle.data.Requests.WebhookRequest;
import com.whatbottle.service.Whatbottleservice;
import io.smooch.client.auth.ApiKeyAuth;
import io.smooch.client.model.MessageResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * Created by gunaass
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/whatbottle")
@CrossOrigin
public class WhatbottleRestController {

    @Autowired
    Whatbottleservice whatbottleservice;


    @RequestMapping(value = {"/getJwtToken"}, method = RequestMethod.GET)
    public ResponseEntity<?> getJwtToken(HttpServletRequest request)
            throws Exception {
        ApiKeyAuth apiKeyAuth = whatbottleservice.generateToken();
        return new ResponseEntity<ApiKeyAuth>(apiKeyAuth, HttpStatus.OK);
    }

    @RequestMapping(value = {"/postReplyMessage"}, method = RequestMethod.POST)
    public ResponseEntity<?> postAMessage(HttpServletRequest request, @RequestParam String userId, @NonNull @RequestBody MessageRequest messageRequest)
            throws Exception {
        MessageResponse messageResponse = whatbottleservice.postRepliesMessage(messageRequest, userId);
        return new ResponseEntity<MessageResponse>(messageResponse, HttpStatus.OK);
    }


    @RequestMapping(value = {"/floodATopic"}, method = RequestMethod.GET)
    public ResponseEntity<?> floodTopic(HttpServletRequest request)
            throws Exception {
        return new ResponseEntity<String>("", HttpStatus.OK);
    }

    @RequestMapping(value = {"/webhook"}, method = RequestMethod.POST)
    public ResponseEntity<?> readAMessage(HttpServletRequest request, @RequestBody WebhookRequest webhookRequest)
            throws Exception {
        log.info(webhookRequest.toString());
        if(Objects.nonNull(webhookRequest.getMessages()) && !webhookRequest.getMessages().get(0).getAuthorId().equalsIgnoreCase("00ubo5auzzt5vbsl90h7"))
            return new ResponseEntity<String>(String.valueOf(whatbottleservice.readAMessage(webhookRequest.getMessages())), HttpStatus.OK);
        else
            return new ResponseEntity<String>("Ping", HttpStatus.OK);
    }
}

