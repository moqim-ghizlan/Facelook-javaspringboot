package moqim.me.facelook.services.impl;

import lombok.RequiredArgsConstructor;
import moqim.me.facelook.domain.entities.Comment;
import moqim.me.facelook.domain.entities.Emotion;
import moqim.me.facelook.domain.entities.Post;
import moqim.me.facelook.domain.entities.Topic;
import moqim.me.facelook.domain.entities.User;
import moqim.me.facelook.domain.enums.EmotionStatus;
import moqim.me.facelook.domain.requests.CreatePostRequest;
import moqim.me.facelook.domain.requests.UpdatePostRequest;
import moqim.me.facelook.repository.CommentRepository;
import moqim.me.facelook.repository.EmotionRepository;
import moqim.me.facelook.repository.PostRepository;
import moqim.me.facelook.repository.TopicRepository;
import moqim.me.facelook.repository.UserRepository;
import moqim.me.facelook.services.PostService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final EmotionRepository emotionRepository;
    private final CommentRepository commentRepository;

    @Override
    public Post getPostById(long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post with id " + id + " not found!"));
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    @Transactional
    public Post createPost(CreatePostRequest createPostRequest, long userId) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found!"));
        Topic topic = topicRepository.findById(createPostRequest.getTopicId())
                .orElseThrow(() -> new IllegalArgumentException("Topic with id " + createPostRequest.getTopicId() + " not found!"));

        Post newPost = new Post();
        newPost.setContent(createPostRequest.getContent());
        newPost.setAuthor(author);
        newPost.setTopic(topic);
        newPost.setPostCount(0);
        return postRepository.save(newPost);
    }

    @Override
    @Transactional
    public Post updatePost(UpdatePostRequest updatePostRequest, long postId) {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post with id " + postId + " not found!"));
        existingPost.setContent(updatePostRequest.getContent());
        return postRepository.save(existingPost);
    }

    @Override
    @Transactional
    public Post updatePost(UpdatePostRequest updatePostRequest, long postId, long userId) {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post with id " + postId + " not found!"));
        if (existingPost.getAuthor() == null || !Objects.equals(existingPost.getAuthor().getId(), userId)) {
            throw new IllegalArgumentException("You are not allowed to edit this post");
        }
        existingPost.setContent(updatePostRequest.getContent());
        return postRepository.save(existingPost);
    }

    @Override
    @Transactional
    public void deletePost(long id) {
        Post post = getPostById(id);
        postRepository.delete(post);
    }

    @Override
    public List<Post> listPostsByTopicId(long topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new IllegalArgumentException("Topic with id " + topicId + " not found!"));
        return topic.getPosts();
    }

    @Override
    public List<Post> listPostsByCreatorId(long creatorId) {
        User user = userRepository.findById(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + creatorId + " not found!"));
        return postRepository.findByAuthor(user);
    }

    @Override
    public Post postByTopicId(long topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new IllegalArgumentException("Topic with id " + topicId + " not found!"));
        return topic.getPosts().stream().findFirst().orElse(null);
    }

    @Override
    public List<Post> getFeed(Long userId) {
        if (userId == null) {
            return postRepository.findAllByOrderByCreatedAtDesc();
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found!"));

        Set<User> following = user.getFollowing();
        List<Post> followedPosts = (following == null || following.isEmpty())
                ? List.of()
                : postRepository.findAllByAuthorInOrderByCreatedAtDesc(following.stream().toList());

        List<Post> allPosts = postRepository.findAllByOrderByCreatedAtDesc();

        LinkedHashSet<Long> seen = new LinkedHashSet<>();
        ArrayList<Post> result = new ArrayList<>();
        for (Post p : followedPosts) {
            if (seen.add(p.getId())) result.add(p);
        }
        for (Post p : allPosts) {
            if (seen.add(p.getId())) result.add(p);
        }
        return result;
    }

    @Override
    @Transactional
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
    @Transactional
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
    @Transactional
    public Post unlikePost(long postId, long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post with id " + postId + " not found!"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found!"));
        emotionRepository.deleteByPostAndUser(post, user);
        return post;
    }

    @Override
    @Transactional
    public Post undislikePost(long postId, long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post with id " + postId + " not found!"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found!"));
        emotionRepository.deleteByPostAndUser(post, user);
        return post;
    }

    @Override
    @Transactional
    public Post addComment(long postId, long userId, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post with id " + postId + " not found!"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found!"));

        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(content)
                .build();
        commentRepository.save(comment);
        return post;
    }

    @Override
    public List<Comment> listComments(long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post with id " + postId + " not found!"));
        return commentRepository.findByPost(post);
    }

    @Override
    @Transactional
    public void deleteComment(long commentId, long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment with id " + commentId + " not found!"));
        if (comment.getUser() == null || !Objects.equals(comment.getUser().getId(), userId)) {
            throw new IllegalArgumentException("You are not allowed to delete this comment");
        }
        commentRepository.delete(comment);
    }

    @Override
    @Transactional
    public Comment likeComment(long commentId, long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment with id " + commentId + " not found!"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found!"));
        comment.getLikes().add(user);
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment unlikeComment(long commentId, long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment with id " + commentId + " not found!"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found!"));
        comment.getLikes().remove(user);
        return commentRepository.save(comment);
    }
}