package com.nicosteinmueller.notes_app.Security.auth;

import com.nicosteinmueller.notes_app.Models.*;
import com.nicosteinmueller.notes_app.Models.api.AuthenticationRequest;
import com.nicosteinmueller.notes_app.Models.api.RegisterRequest;
import com.nicosteinmueller.notes_app.Repositorys.TokenRepository;
import com.nicosteinmueller.notes_app.Repositorys.UserRepository;
import com.nicosteinmueller.notes_app.Security.config.JwtService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static com.mongodb.assertions.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthenticationServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    JwtService jwtService;
    @Mock
    AuthenticationManager authenticationManager;
    @InjectMocks
    AuthenticationService authenticationService;

    @AfterEach
    void cleanUpDatabase() {
        mongoTemplate.getDb().drop();
    }

    @BeforeEach
    void setUp(){
        Settings settings = new Settings(true, Language.ENGLISH);
        User user = new User("123", "first", "last", "first@last.com", "passHash",
                LocalDateTime.of(2023, 3, 20, 12, 0), settings, Role.USER);
        userRepository.save(user);
        Mockito.when(jwtService.generateToken(Mockito.any())).thenReturn("token");

        authenticationService = new AuthenticationService(userRepository, tokenRepository, passwordEncoder, jwtService, authenticationManager);

    }
    @Test
    @Order(2)
    void registerSuccess() {
        var register = RegisterRequest.builder()
                .firstName("first")
                .lastName("last")
                .email("newmail@mail")
                .password("passHash")
                .build();
        var response = authenticationService.register(register);
        assertTrue(userRepository.findUserByEmail("newmail@mail").isPresent());

        assertTrue(tokenRepository.findTokenByToken(response.getToken()).isPresent());
    }

    @Test
    @Order(1)
    void registerFailDoubleUsername() {
        var register = RegisterRequest.builder()
                .firstName("first")
                .lastName("last")
                .email("first@last.com")
                .password("passwordHash")
                .build();
        when(passwordEncoder.encode(Mockito.anyString())).thenReturn("passHash");
        assertThrows(DuplicateKeyException.class, () -> authenticationService.register(register));
    }

    @Test
    @Order(10)
    void authenticateWithValidCredentials() {
        var request = AuthenticationRequest.builder()
                .email("first@last.com")
                .password("passwordHash")
                .build();

        var response = authenticationService.authenticate(request);

        assertTrue(tokenRepository.findTokenByToken(response.getToken()).isPresent());
    }

    @Test
    @Order(11)
    void authenticateWithInvalidCredentials() {
        var request = AuthenticationRequest.builder()
                .email("wrong@mail")
                .password("passwordHash")
                .build();

        assertThrows(NoSuchElementException.class, () -> authenticationService.authenticate(request));

    }
}