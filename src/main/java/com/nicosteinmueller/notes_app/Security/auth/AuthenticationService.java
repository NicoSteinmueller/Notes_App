package com.nicosteinmueller.notes_app.Security.auth;

import com.nicosteinmueller.notes_app.Models.*;
import com.nicosteinmueller.notes_app.Models.api.AuthenticationRequest;
import com.nicosteinmueller.notes_app.Models.api.AuthenticationResponse;
import com.nicosteinmueller.notes_app.Models.api.RegisterRequest;
import com.nicosteinmueller.notes_app.Repositorys.TokenRepository;
import com.nicosteinmueller.notes_app.Repositorys.UserRepository;
import com.nicosteinmueller.notes_app.Security.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        var savedUser = userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        saveUserToken(savedUser, jwt);

        return AuthenticationResponse.builder().token(jwt).build();
    }

    public AuthenticationResponse authenticate (AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var user = userRepository.findUserByEmail(request.getEmail()).orElseThrow();
        var jwt = jwtService.generateToken(user);
        // revokeAllTokens(user); TODO check if needed
        saveUserToken(user, jwt);

        return AuthenticationResponse.builder().token(jwt).build();
    }

    private void saveUserToken(User user, String jwt) {
        var token = Token.builder()
                .user(user)
                .token(jwt)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllTokens (User user) {
        var validUserTokens = tokenRepository.findAllByUserAndExpiredFalseOrRevokedIsFalse(user);
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

}
