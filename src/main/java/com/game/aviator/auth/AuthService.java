package com.game.aviator.auth;

import com.game.aviator.auth.dto.AuthResponse;
import com.game.aviator.auth.dto.LoginRequest;
import com.game.aviator.auth.dto.RegisterRequest;
import com.game.aviator.user.Role;
import com.game.aviator.user.User;
import com.game.aviator.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Phone number already exist! ");
        }

//        if (userRepository.existsByEmail(request.email())) {
//            throw new RuntimeException("Email is already registered");
//        }

        User user = User.builder()
                .username(request.username())
                .name(request.name())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .enabled(true)
                .build();

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser);

        return new AuthResponse(
                token,
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getName(),
                savedUser.getRole().name()
        );
    }

    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user);
        String myname ="korir";

        return new AuthResponse(
                token,
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getRole().name()
        );
    }

    public AuthResponse getCurrentUser(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new AuthResponse(
                null,
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getRole().name()
        );
    }
}