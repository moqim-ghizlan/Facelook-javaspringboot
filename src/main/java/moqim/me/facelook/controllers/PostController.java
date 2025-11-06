package moqim.me.facelook.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import moqim.me.facelook.domain.dtos.*;
import moqim.me.facelook.domain.entities.Comment;
import moqim.me.facelook.domain.entities.Post;
import moqim.me.facelook.domain.requests.CreatePostRequest;
import moqim.me.facelook.domain.requests.UpdatePostRequest;
import moqim.me.facelook.mappers.PostMapper;
import moqim.me.facelook.services.PostService;
import moqim.me.facelook.security.FBUserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final PostMapper postMapper;


    @GetMapping(path = "/all")
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<PostDto> allPosts = postService.getAllPosts().stream().map(postMapper::toDto).toList();
        return ResponseEntity.ok(allPosts);
    }

    @GetMapping(path = "/one/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable long id){
        PostDto postDtp = postMapper.toDto(postService.getPostById(id));
        return ResponseEntity.ok(postDtp);
    }


        @PostMapping(path = "/create")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody CreatePostRequestDto createPostRequestDto, @AuthenticationPrincipal FBUserDetails user){
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        CreatePostRequest createPostRequest = postMapper.toCreatePostRequest(createPostRequestDto);
        Post createdPost = postService.createPost(createPostRequest, user.getId());
        PostDto postDto = postMapper.toDto(createdPost);
        return ResponseEntity.ok(postDto);
    }


    @PutMapping(path = "/one/{id}/update")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable long id,
            @Valid @RequestBody UpdatePostRequestDto updatePostRequestDto,
            @AuthenticationPrincipal FBUserDetails user
    ) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        UpdatePostRequest updatePostRequest1 = postMapper.updatePostRequest(updatePostRequestDto);
        Post updatedPost = postService.updatePost(updatePostRequest1, id, user.getId());
        PostDto postDto = postMapper.toDto(updatedPost);
        return ResponseEntity.ok(postDto);
    }

    @DeleteMapping(path = "/one/{id}/delete")
    public ResponseEntity<String> deletePost(@PathVariable long id) {
        postService.deletePost(id);
        return ResponseEntity.ok("Post deleted");
    }

    @PutMapping(path = "/one/{id}/like")
    public ResponseEntity<PostDto> likePost(@PathVariable long id, @AuthenticationPrincipal FBUserDetails user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        Post post = postService.likePost(id, user.getId());
        return ResponseEntity.ok(postMapper.toDto(post));
    }

    @PutMapping(path = "/one/{id}/dislike")
    public ResponseEntity<PostDto> dislikePost(@PathVariable long id, @AuthenticationPrincipal FBUserDetails user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        Post post = postService.dislikePost(id, user.getId());
        return ResponseEntity.ok(postMapper.toDto(post));
    }

    @PutMapping(path = "/one/{id}/unlike")
    public ResponseEntity<PostDto> unlikePost(@PathVariable long id, @AuthenticationPrincipal FBUserDetails user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        Post post = postService.unlikePost(id, user.getId());
        return ResponseEntity.ok(postMapper.toDto(post));
    }

    @PutMapping(path = "/one/{id}/undislike")
    public ResponseEntity<PostDto> undislikePost(@PathVariable long id, @AuthenticationPrincipal FBUserDetails user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        Post post = postService.undislikePost(id, user.getId());
        return ResponseEntity.ok(postMapper.toDto(post));
    }

    @PostMapping(path = "/one/{id}/comments")
    public ResponseEntity<PostDto> addComment(
            @PathVariable long id,
            @Valid @RequestBody CreateCommentRequestDto createCommentRequestDto,
            @AuthenticationPrincipal FBUserDetails user
    ) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        Post post = postService.addComment(id, user.getId(), createCommentRequestDto.getContent());
        return ResponseEntity.ok(postMapper.toDto(post));
    }

    @GetMapping(path = "/one/{id}/comments")
    public ResponseEntity<List<moqim.me.facelook.domain.dtos.CommentDto>> listComments(
            @PathVariable long id
    ) {
        List<Comment> comments = postService.listComments(id);
        List<CommentDto> dos = comments.stream().map(comment -> CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .author(moqim.me.facelook.domain.dtos.AuthorDto.builder()
                        .id(comment.getUser().getId())
                        .build())
                .build()).collect(Collectors.toList());
        return ResponseEntity.ok(dos);
    }

    @DeleteMapping(path = "/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable long commentId,
            @AuthenticationPrincipal FBUserDetails user
    ) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        postService.deleteComment(commentId, user.getId());
        return ResponseEntity.noContent().build();
    }

}
