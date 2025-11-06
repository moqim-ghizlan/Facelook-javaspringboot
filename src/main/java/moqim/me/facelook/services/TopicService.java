package moqim.me.facelook.services;

import moqim.me.facelook.domain.entities.Topic;

import java.util.List;

public interface TopicService {
    List<Topic> listTopics();
    Topic createTopic(Topic topic);
    void deleteTopic(long id);
    Topic getTopicById(long id);
    Topic updateTopic(Topic topic);
    List<Topic> searchTopics(String name);
    List<Topic> listTopicsByCreatorId(long creatorId);
    Topic topicByPostId(long postId);

}
