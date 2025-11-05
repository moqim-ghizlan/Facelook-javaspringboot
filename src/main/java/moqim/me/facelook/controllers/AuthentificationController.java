package moqim.me.facelook.controllers;

import lombok.RequiredArgsConstructor;
import moqim.me.facelook.domain.dtos.AuthentificationResponse;
import moqim.me.facelook.domain.dtos.RegisterRequest;
import moqim.me.facelook.domain.requests.LoginRequest;
import moqim.me.facelook.services.AuthentificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthentificationController {
    private final AuthentificationService authService;

    @PostMapping(path = "/login")
    public ResponseEntity<AuthentificationResponse> login(@RequestBody LoginRequest loginRequest) {
        UserDetails userDetails = authService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        String tokenValue = authService.generateToken(userDetails);
        AuthentificationResponse authResponse = AuthentificationResponse.builder()
                .token(tokenValue)
                .expiresIn(24 * 60 * 60)
                .build();

        return ResponseEntity.ok(authResponse);
    }



    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


}
