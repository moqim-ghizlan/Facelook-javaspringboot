package moqim.me.facelook.services;

import moqim.me.facelook.domain.entities.User;

import java.util.Optional;

public interface UserService {
    User getUserById(long id);
    User getUserByEmail(String email);
    User createUser(User user);
    User updateUser(long id, User user);
    void deleteUser(long id);
}
