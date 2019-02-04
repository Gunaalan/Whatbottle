package com.whatbottle.util;

import com.lithium.mineraloil.api.lia.*;
import com.lithium.mineraloil.api.lia.api.models.*;
import com.lithium.mineraloil.api.lia.api.v2.endpoints.MessageReplyV2Endpoints;
import com.lithium.mineraloil.api.lia.api.v2.models.MessageV2Response;
import com.lithium.mineraloil.api.rest.APICall;
import retrofit2.Retrofit;

public class MessageReply{

    private LIAAPIConnection connection;
    private MessageReplyV2Endpoints endpoints;
    private static final String TYPE = "message";

    public MessageReply(LIAAPIConnection connection) {
        this.connection = connection;
        Retrofit retrofit = LIARetrofitFactory.createJsonBuilder(connection).baseUrl(LIAV2APIConnection.getBaseUrl(connection.getHttpHost())).client(RetrofitClient.basicAuthClient(connection)).build();
        this.endpoints = (MessageReplyV2Endpoints)retrofit.create(MessageReplyV2Endpoints.class);
    }

    public MessageV2Response postMessageReply(Board board, Message message, User author, Parent parent) {
        return (MessageV2Response)((JsonResponse)(new APICall(this.endpoints.postMessageReply("message", message.getSubject(), message.getBody(), JsonHelper.convertToJson(board), JsonHelper.convertToJson(author), JsonHelper.convertToJson(parent)))).makeRequest()).getData();

    }
}
