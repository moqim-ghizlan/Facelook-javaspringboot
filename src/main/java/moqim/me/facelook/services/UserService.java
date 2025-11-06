package moqim.me.facelook.services;

import moqim.me.facelook.domain.entities.User;

import java.util.Optional;
import java.util.List;

public interface UserService {
    User getUserById(long id);
    User getUserByEmail(String email);
    User createUser(User user);
    User updateUser(long id, User user);
    void deleteUser(long id);

    // Follow feature
    void follow(long followerId, long followingId);
    void unfollow(long followerId, long followingId);
    List<User> getFollowers(long userId);
    List<User> getFollowing(long userId);

    Long getFollowersCount(long userId);
    Long getFollowingCount(long userId);
}
