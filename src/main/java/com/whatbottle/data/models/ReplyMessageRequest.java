package com.whatbottle.data.models;


import com.whatbottle.data.Requests.MessageRequest;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Repository;

import java.util.Date;


/**
 *
 * @author gunaas
 *
 */

@Document(collection = "ReplyMessageRequestQueue")
@Data
public class ReplyMessageRequest {
    String id;

    String message;

    String userName;

    String topicName;

    String userId;

    Date createdDate = new Date();

    public ReplyMessageRequest(String message) {
        this.message = message;
    }

    public ReplyMessageRequest() {
    }
}
