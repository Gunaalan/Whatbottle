package com.whatbottle.repository;


import com.whatbottle.data.models.TopicMessageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 *
 * @author gunaas
 *
 */
@Repository
public interface TopicMessageRequestRepository extends MongoRepository<TopicMessageRequest, String> {
    TopicMessageRequest save(TopicMessageRequest topicMessageRequest);

    List<TopicMessageRequest> findAll();

}
