package moqim.me.facelook.services;

import moqim.me.facelook.domain.entities.Post;
import moqim.me.facelook.domain.entities.Comment;
import moqim.me.facelook.domain.requests.CreatePostRequest;
import moqim.me.facelook.domain.requests.UpdatePostRequest;

import java.util.List;

public interface PostService {

    Post getPostById(long id);
    List<Post> getAllPosts();
    Post createPost(CreatePostRequest createPostRequest, long userId);
    Post updatePost(UpdatePostRequest updatePostRequest, long postId);
    Post updatePost(UpdatePostRequest updatePostRequest, long postId, long userId);
    void deletePost(long id);
    List<Post> listPostsByTopicId(long topicId);
    List<Post> listPostsByCreatorId(long creatorId);
    Post postByTopicId(long topicId);

    Post likePost(long postId, long userId);
    Post dislikePost(long postId, long userId);
    Post unlikePost(long postId, long userId);
    Post undislikePost(long postId, long userId);

    Post addComment(long postId, long userId, String content);
    List<Comment> listComments(long postId);
    void deleteComment(long commentId, long userId);
}
