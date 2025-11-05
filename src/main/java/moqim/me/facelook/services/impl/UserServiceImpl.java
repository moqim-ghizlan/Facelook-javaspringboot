package moqim.me.facelook.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import moqim.me.facelook.domain.entities.User;
import moqim.me.facelook.repository.UserRepository;
import moqim.me.facelook.services.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));
    }



    @Override
    public User createUser(User user) {
        String email = user.getEmail();
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new EntityNotFoundException("User with email " + email + " already exists");
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUser(long id, User user) {
        User existingUser = this.getUserById(id);
        if(existingUser == null) {throw new EntityNotFoundException("User with id " + id + " not found");}
        existingUser.setName(user.getName());
        existingUser.setBio(user.getBio());
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(long id) {
        User user = this.getUserById(id);
        if (null != user) {
            userRepository.delete(user);
        }

    }

    // Follow feature implementations
    @Override
    @Transactional
    public void follow(long followerId, long followingId) {
        if (followerId == followingId) return;
        User follower = getUserById(followerId);
        User following = getUserById(followingId);
        follower.follow(following);
        userRepository.save(follower); // owning side persists
    }

    @Override
    @Transactional
    public void unfollow(long followerId, long followingId) {
        if (followerId == followingId) return;
        User follower = getUserById(followerId);
        User following = getUserById(followingId);
        follower.unfollow(following);
        userRepository.save(follower);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getFollowers(long userId) {
        User user = getUserById(userId);
        return user.getFollowers().stream().toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getFollowing(long userId) {
        User user = getUserById(userId);
        return user.getFollowing().stream().toList();
    }

    @Override
    public Long getFollowersCount(long userId) {
        return (long) getUserById(userId).getFollowers().size();
    }

    @Override
    public Long getFollowingCount(long userId) {
        return (long) getUserById(userId).getFollowing().size();
    }
}
