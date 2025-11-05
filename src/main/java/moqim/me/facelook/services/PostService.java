package moqim.me.facelook.services;

import moqim.me.facelook.domain.entities.Post;
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
}
