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

@Document(collection = "TopicMessageRequestQueue")
@Data
public class TopicMessageRequest {
    @Id
    String id;

    @Indexed(background =  true)
    Date createdDate;

    MessageRequest messageRequest;

    @Indexed(background =  true)
    String topicId;

    String boardId;

}
