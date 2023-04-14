package com.nicosteinmueller.notes_app.controllers;

import com.nicosteinmueller.notes_app.Controllers.AuthenticationController;
import com.nicosteinmueller.notes_app.Models.User;
import com.nicosteinmueller.notes_app.Models.api.AuthenticationRequest;
import com.nicosteinmueller.notes_app.Models.api.AuthenticationResponse;
import com.nicosteinmueller.notes_app.Models.api.RegisterRequest;
import com.nicosteinmueller.notes_app.Repositorys.UserRepository;
import com.nicosteinmueller.notes_app.Security.auth.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
    @Mock
    AuthenticationService authenticationService;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    AuthenticationController authenticationController;


    @Test
    void registerSuccess(){
        Mockito.when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        var authResp = AuthenticationResponse.builder()
                .token("token")
                .build();
        Mockito.when(authenticationService.register(Mockito.any())).thenReturn(authResp);
        var request = RegisterRequest.builder()
                .firstName("first")
                .lastName("last")
                .email("first@last")
                .password("passwordHash")
                .build();

        var response = authenticationController.register(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResp, response.getBody());
    }

    @Test
    void registerFailBecauseUserAlreadyExists(){
        Mockito.when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(Optional.of(new User()));
        var request = RegisterRequest.builder()
                .firstName("first")
                .lastName("last")
                .email("first@last")
                .password("passwordHash")
                .build();

        var response = authenticationController.register(request);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void authenticateSuccess(){
        var authResp = AuthenticationResponse.builder()
                .token("token")
                .build();
        Mockito.when(authenticationService.authenticate(Mockito.any())).thenReturn(authResp);
        var request = AuthenticationRequest.builder()
                .email("new@mail")
                .password("passHash")
                .build();

        var response = authenticationController.authenticate(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResp, response.getBody());
    }

    @Test
    void authenticationFailBecauseInvalidCredentials(){
        Mockito.when(authenticationService.authenticate(Mockito.any(AuthenticationRequest.class))).thenThrow(BadCredentialsException.class);
        var request = AuthenticationRequest.builder()
                .email("new@mail")
                .password("passHash")
                .build();
        assertThrows(BadCredentialsException.class, () -> authenticationController.authenticate(request));
    }

}
