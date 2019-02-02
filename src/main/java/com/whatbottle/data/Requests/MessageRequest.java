package com.whatbottle.data.Requests;

import lombok.Data;

@Data
public class MessageRequest {

    public MessageRequest(String message) {
        this.message = message;
    }

    public MessageRequest() {
    }

    String message;
}
