package com.whatbottle.data.Requests;

import lombok.Data;

@Data
public class MessageRequest {
    String message;

    public MessageRequest(String message) {
        this.message = message;
    }

    public MessageRequest() {
    }

}
