package com.nicosteinmueller.notes_app.Security;

import com.nicosteinmueller.notes_app.Repositorys.TokenRepository;
import com.nicosteinmueller.notes_app.Repositorys.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    TokenRepository tokenRepository;

    @Test
    void register() {
    }

    @Test
    void authenticate() {
    }
}