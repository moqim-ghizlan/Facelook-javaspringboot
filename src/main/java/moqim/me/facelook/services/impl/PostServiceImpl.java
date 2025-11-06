package moqim.me.facelook.services.impl;

import lombok.RequiredArgsConstructor;
import moqim.me.facelook.domain.entities.Post;
import moqim.me.facelook.domain.entities.Topic;
import moqim.me.facelook.domain.entities.User;
import moqim.me.facelook.domain.enums.EmotionStatus;
import moqim.me.facelook.domain.requests.CreatePostRequest;
import moqim.me.facelook.domain.requests.UpdatePostRequest;
import moqim.me.facelook.repository.EmotionRepository;
import moqim.me.facelook.repository.PostRepository;
import moqim.me.facelook.repository.TopicRepository;
import moqim.me.facelook.repository.UserRepository;
import moqim.me.facelook.services.PostService;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.stereotype.Service;

import java.util.List;

import moqim.me.facelook.domain.entities.Emotion;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final EmotionRepository emotionRepository;


    @Override
    public Post getPostById(long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Post with id " + id + " not found!")
        );
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post createPost(CreatePostRequest createPostRequest, long userId) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found!"));
        Topic topic = topicRepository.findById(createPostRequest.getTopicId())
                .orElseThrow(() -> new IllegalArgumentException("Topic with id " + createPostRequest.getTopicId() + " not found!"));

        Post newPost = new Post();
        newPost.setTitle(createPostRequest.getTitle());
        newPost.setAuthor(author);
        newPost.setTopic(topic);
        newPost.setPostCount(0);
        return postRepository.save(newPost);
    }

    @Override
    public Post updatePost(UpdatePostRequest updatePostRequest, long id) {

        Post existingPost = postRepository.findById(id).orElseThrow(() -> new IllegalIdentifierException("Post with id " + id + " not found!"));
        existingPost.setTitle(updatePostRequest.getTitle());
        return postRepository.save(existingPost);
    }

    @Override
    public Post updatePost(UpdatePostRequest updatePostRequest, long id, long userId) {
        Post existingPost = postRepository.findById(id).orElseThrow(() -> new IllegalIdentifierException("Post with id " + id + " not found!"));
        if (existingPost.getAuthor() == null || existingPost.getAuthor().getId() != userId) {
            throw new IllegalArgumentException("You are not allowed to edit this post");
        }

        existingPost.setTitle(updatePostRequest.getTitle());
        return postRepository.save(existingPost);
    }

    @Override
    public void deletePost(long id) {
        Post post = getPostById(id);
        postRepository.delete(post);
    }

    @Override
    public List<Post> listPostsByTopicId(long topicId) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new IllegalArgumentException("Topic with id " + topicId + " not found!"));
        return topic.getPosts();
    }

    @Override
    public List<Post> listPostsByCreatorId(long creatorId) {
        User user = userRepository.findById(creatorId).orElseThrow(() -> new IllegalArgumentException("User with id " + creatorId + " not found!"));
        return postRepository.findByAuthor(user);
    }

    @Override
    public Post postByTopicId(long topicId) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new IllegalArgumentException("Topic with id " + topicId + " not found!"));
        return topic.getPosts().stream().findFirst().orElse(null);
    }

    @Override
    public Post likePost(long postId, long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post with id " + postId + " not found!"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found!"));

        Emotion emotion = emotionRepository.findByPostAndUser(post, user).orElse(null);
        if (emotion == null) {
            emotion = Emotion.builder().post(post).user(user).status(EmotionStatus.LIKE).build();
        } else {
            emotion.setStatus(EmotionStatus.LIKE);
        }
        emotionRepository.save(emotion);
        return post;
    }

    @Override
    public Post dislikePost(long postId, long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post with id " + postId + " not found!"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found!"));

        Emotion emotion = emotionRepository.findByPostAndUser(post, user).orElse(null);
        if (emotion == null) {
            emotion = Emotion.builder().post(post).user(user).status(EmotionStatus.DISLIKE).build();
        } else {
            emotion.setStatus(EmotionStatus.DISLIKE);
        }
        emotionRepository.save(emotion);
        return post;
    }

    @Override
    public Post unlikePost(long postId, long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post with id " + postId + " not found!"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found!"));
        emotionRepository.findByPostAndUser(post, user).ifPresent(emotionRepository::delete);
        return post;
    }

    @Override
    public Post undislikePost(long postId, long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post with id " + postId + " not found!"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found!"));
        emotionRepository.findByPostAndUser(post, user).ifPresent(emotionRepository::delete);
        return post;
    }

}
