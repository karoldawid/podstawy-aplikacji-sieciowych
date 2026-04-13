package sfs.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import sfs.repository.UserRepository;
import sfs.model.User;
import sfs.rest.dto.AuthRequest;
import sfs.rest.dto.AuthResponse;
import sfs.security.JwtService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthRestController(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByLogin(request.getLogin())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // generacja tokenu
        String token = jwtService.generateToken(authenticate.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails ?
                (org.springframework.security.core.userdetails.UserDetails) authenticate.getPrincipal() : null);

        // zwracamy dane do frontu
        return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getRole(), user.getLogin()));
    }
}