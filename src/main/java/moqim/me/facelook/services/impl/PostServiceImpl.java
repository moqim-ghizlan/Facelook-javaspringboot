package moqim.me.facelook.services.impl;

import lombok.RequiredArgsConstructor;
import moqim.me.facelook.domain.entities.Post;
import moqim.me.facelook.domain.requests.CreatePostRequest;
import moqim.me.facelook.domain.requests.UpdatePostRequest;
import moqim.me.facelook.repository.PostRepository;
import moqim.me.facelook.services.PostService;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;


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
    public Post createPost(CreatePostRequest createPostRequest) {
        Post newPost = new Post();
        newPost.setTitle(createPostRequest.getTitle());
        return postRepository.save(newPost);

    }

    @Override
    public Post updatePost(UpdatePostRequest updatePostRequest, long id) {
        Post existingPost = postRepository.findById(id).orElseThrow(() -> new IllegalIdentifierException("Post with id " + id + " not found!"));

        existingPost.setTitle(updatePostRequest.getTitle());
        return postRepository.save(existingPost);


    }

    @Override
    public void deletePost(long id) {
        Post post = getPostById(id);
        System.out.println(post);
        postRepository.delete(post);
    }

}
