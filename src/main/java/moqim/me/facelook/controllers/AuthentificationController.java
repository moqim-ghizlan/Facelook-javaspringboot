package moqim.me.facelook.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import moqim.me.facelook.domain.dtos.AuthentificationResponse;
import moqim.me.facelook.domain.dtos.RegisterRequest;
import moqim.me.facelook.domain.requests.LoginRequest;
import moqim.me.facelook.services.AuthentificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthentificationController {
    private final AuthentificationService authService;

    @PostMapping(path = "/login")
    public ResponseEntity<AuthentificationResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        UserDetails userDetails = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        AuthentificationResponse authResponse = loginUser(userDetails);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<AuthentificationResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        System.out.println("get called");
        UserDetails userDetails = authService.register(registerRequest);
        AuthentificationResponse authResponse = loginUser(userDetails);
        return ResponseEntity.ok(authResponse);
    }

    public AuthentificationResponse loginUser(UserDetails userDetails) {
        String tokenValue = authService.generateToken(userDetails);
        return  AuthentificationResponse.builder()
                .token(tokenValue)
                .expiresIn(24 * 60 * 60)
                .build();

    }


    @GetMapping(path = "/logout")
    public ResponseEntity<Map<String, String>> logout() {
        return ResponseEntity.ok(Map.of(
                "message", "Logged out successfully. Remove the JWT on the client side."
        ));
    }
}
