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

    @RequestMapping(value = {"/postAMessage"}, method = RequestMethod.POST)
    public ResponseEntity<?> postAMessage(HttpServletRequest request, @RequestParam String userId, @NonNull @RequestBody MessageRequest messageRequest)
            throws Exception {
        MessageResponse messageResponse = whatbottleservice.postAMessage(messageRequest, userId);
        return new ResponseEntity<MessageResponse>(messageResponse, HttpStatus.OK);
    }


    @RequestMapping(value = {"/floodATopic"}, method = RequestMethod.GET)
    public ResponseEntity<?> floodTopic(HttpServletRequest request)
            throws Exception {
        return new ResponseEntity<String>("", HttpStatus.OK);
    }

//    @RequestMapping(value = {"/webhook"}, method = RequestMethod.POST)
//    public ResponseEntity<?> readAMessage(HttpServletRequest request, @RequestBody WebhookRequest webhookRequest)
//            throws Exception {
//        log.info(webhookRequest.toString());
//        //whatbottleservice.readAMessage(webhookRequest.getMessages());
//        return new ResponseEntity<String>("Done", HttpStatus.OK);
//    }
}

