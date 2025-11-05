package moqim.me.facelook.controllers;

import lombok.RequiredArgsConstructor;
import moqim.me.facelook.domain.dtos.AuthorDto;
import moqim.me.facelook.domain.entities.User;
import moqim.me.facelook.mappers.UserMapper;
import moqim.me.facelook.security.FBUserDetails;
import moqim.me.facelook.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;


    @PostMapping(path = "/{id}/follow")
    public ResponseEntity<Void> follow(@PathVariable("id") long targetUserId,
                                       @AuthenticationPrincipal FBUserDetails user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        userService.follow(user.getId(), targetUserId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{id}/follow")
    public ResponseEntity<Void> unfollow(@PathVariable("id") long targetUserId,
                                         @AuthenticationPrincipal FBUserDetails user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        userService.unfollow(user.getId(), targetUserId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/{id}/followers")
    public ResponseEntity<List<AuthorDto>> getFollowers(@PathVariable("id") long userId) {
        List<AuthorDto> followers = userService.getFollowers(userId)
                .stream()
                .map(userMapper::toAuthorDto)
                .toList();
        return ResponseEntity.ok(followers);
    }
    @GetMapping(path = "{id}/followers/count")
    public ResponseEntity<Long> getFollowersCount(@PathVariable("id") long userId) {
        return ResponseEntity.ok(userService.getFollowersCount(userId));
    }

    @GetMapping(path = "/{id}/following")
    public ResponseEntity<List<AuthorDto>> getFollowing(@PathVariable("id") long userId) {
        List<AuthorDto> following = userService.getFollowing(userId)
                .stream()
                .map(userMapper::toAuthorDto)
                .toList();
        return ResponseEntity.ok(following);
    }
    @GetMapping(path = "{id}/following/count")
    public ResponseEntity<Long> getFollowingCount(@PathVariable("id") long userId) {
        return ResponseEntity.ok(userService.getFollowingCount(userId));
    }
}
