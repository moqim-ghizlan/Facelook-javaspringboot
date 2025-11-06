package moqim.me.facelook.services.impl;

import lombok.RequiredArgsConstructor;
import moqim.me.facelook.domain.entities.Post;
import moqim.me.facelook.domain.entities.Topic;
import moqim.me.facelook.domain.entities.User;
import moqim.me.facelook.repository.PostRepository;
import moqim.me.facelook.repository.TopicRepository;
import moqim.me.facelook.repository.UserRepository;
import moqim.me.facelook.services.TopicService;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService
{

    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public List<Topic> listTopics() {
        return topicRepository.findAll();
    }

    @Override
    public Topic createTopic(Topic topic) {
        if (topic.getCreator() == null || topic.getCreator().getId() == 0) {
            throw new IllegalIdentifierException("Creator is required to create a topic");
        }
        User creator = userRepository.findById(topic.getCreator().getId()).orElseThrow(
                () -> new IllegalIdentifierException("User with id " + topic.getCreator().getId() + " does not exist")
        );
        topic.setCreator(creator);
        return topicRepository.save(topic);
    }

    @Override
    public void deleteTopic(long id) {
        Topic topic = getTopicById(id);
        topicRepository.delete(topic);
    }

    @Override
    public Topic getTopicById(long id) {
        return topicRepository.findById(id).orElseThrow(
                () -> new IllegalIdentifierException("Topic with id " + id + " does not exist")
        );
    }

    @Override
    public Topic updateTopic(Topic topic) {
        Topic existingTopic = getTopicById(topic.getId());
        existingTopic.setName(topic.getName());
        return topicRepository.save(existingTopic);
    }

    @Override
    public List<Topic> searchTopics(String name) {
        return topicRepository.findByNameContaining(name);
    }

    @Override
    public List<Topic> listTopicsByCreatorId(long creatorId) {
        User user = userRepository.findById(creatorId).orElseThrow(
                () -> new IllegalIdentifierException("User with id " + creatorId + " does not exist")
        );
        return topicRepository.findByCreator(user);
    }

    @Override
    public Topic topicByPostId(long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalIdentifierException("Post with id " + postId + " does not exist")
        );
        return post.getTopic();
    }
}
