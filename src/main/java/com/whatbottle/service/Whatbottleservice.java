package com.whatbottle.service;

import com.whatbottle.data.Requests.MessageRequest;
import io.smooch.client.auth.ApiKeyAuth;
import io.smooch.client.model.MessageResponse;

import java.util.List;

public interface Whatbottleservice {
    public ApiKeyAuth generateToken() throws Exception;

    public MessageResponse postAMessage(MessageRequest messageRequest, String userId) throws Exception;
}
