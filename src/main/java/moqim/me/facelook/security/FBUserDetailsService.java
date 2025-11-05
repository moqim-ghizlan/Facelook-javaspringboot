package moqim.me.facelook.security;

import lombok.RequiredArgsConstructor;
import moqim.me.facelook.domain.entities.User;
import moqim.me.facelook.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor

public class FBUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with email: " + email));
        return new FBUserDetails(user);
    }
}
