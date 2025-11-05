package moqim.me.facelook.services;

import moqim.me.facelook.domain.dtos.RegisterRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthentificationService {

    UserDetails authenticate(String email, String password);
    String generateToken(UserDetails userDetails);
    UserDetails validateToken(String token);

}
