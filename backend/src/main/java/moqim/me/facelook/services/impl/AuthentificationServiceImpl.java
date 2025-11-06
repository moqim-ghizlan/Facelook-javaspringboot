package moqim.me.facelook.services.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import moqim.me.facelook.domain.dtos.RegisterRequest;
import moqim.me.facelook.domain.entities.User;
import moqim.me.facelook.repository.UserRepository;
import moqim.me.facelook.services.AuthentificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class AuthentificationServiceImpl implements AuthentificationService {
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Value("${jwt.secret.key:xhkTdTfcS-LrtHAMr_QQhRP-HLhGGV}")
    private String secretKey;


    @Override
    public UserDetails authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        return userDetailsService.loadUserByUsername(email);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 86400000L))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public UserDetails validateToken(String token) {
        return userDetailsService.loadUserByUsername(extractUsername(token));
    }


    private String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();

    }

    private Key getSigningKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public UserDetails register(RegisterRequest registerRequest) {
        validateRegisterRequest(registerRequest);

        User newUser = User.builder()
                .name(registerRequest.getName())
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword1()))
                .bio(registerRequest.getBio())
                .build();

        userRepository.save(newUser);
        return userDetailsService.loadUserByUsername(newUser.getUsername());
    }


    private void validateRegisterRequest(RegisterRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(u -> {
            throw new IllegalArgumentException("Email already exists");
        });
        
        userRepository.findByUsername(request.getUsername()).ifPresent(u -> {
            throw new IllegalArgumentException("Username already exists");
        });

        String password = request.getPassword1();
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must have at least 8 characters");
        }
        if (!password.equals(request.getPassword2())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$")) {
            throw new IllegalArgumentException("Password must include upper, lower, number, and special character");
        }

        if (request.getBio() != null && request.getBio().length() > 250) {
            throw new IllegalArgumentException("Bio must not exceed 250 characters");
        }

        validateStringLength("Name", request.getName(), 6, 30);

        validateStringLength("Username", request.getUsername(), 6, 30);

        validateStringLength("Email", request.getEmail(), 3, 30);
    }

    private void validateStringLength(String field, String value, int min, int max) {
        if (value == null || value.length() < min || value.length() > max) {
            throw new IllegalArgumentException(field + " must be between " + min + " and " + max + " characters");
        }
    }
}
