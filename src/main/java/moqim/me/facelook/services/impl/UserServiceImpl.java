package moqim.me.facelook.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import moqim.me.facelook.domain.entities.User;
import moqim.me.facelook.repository.UserRepository;
import moqim.me.facelook.services.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setBio(user.getBio());
        return userRepository.save(newUser);
    }

    @Override
    public void deleteUser(long id) {
        User user = this.getUserById(id);
        if (null != user) {
            userRepository.delete(user);
        }

    }
}
