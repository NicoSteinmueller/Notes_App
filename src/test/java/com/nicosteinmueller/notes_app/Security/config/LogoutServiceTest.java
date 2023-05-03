package com.nicosteinmueller.notes_app.Security.config;

import com.nicosteinmueller.notes_app.Models.*;
import com.nicosteinmueller.notes_app.Repositorys.TokenRepository;
import com.nicosteinmueller.notes_app.Repositorys.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataMongoTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class LogoutServiceTest {
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @InjectMocks
    LogoutService logoutService;

    @BeforeEach
    void setUp(){
        logoutService = new LogoutService(tokenRepository);

        Settings settings = new Settings(true, Language.ENGLISH);
        User user = new User("123", "first", "last", "first@last.com", "passHash",
                LocalDateTime.of(2023, 3, 20, 12, 0), settings, Role.USER);
        Token token = new Token("abc", "token", TokenType.BEARER, false, false, user);
        userRepository.save(user);
        tokenRepository.save(token);
    }

    @AfterEach
    void cleanUpDatabase() {
        mongoTemplate.getDb().drop();
    }

    @Test
    void logout_success(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        logoutService.logout(request, response, null);
        var result = tokenRepository.findTokenByToken("token");

        assertTrue(result.isPresent());
        assertTrue(result.get().isExpired());
        assertTrue(result.get().isRevoked());
    }

    @Test
    void logout_fail_because_missing_authorization(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        logoutService.logout(request, response, null);
        var result = tokenRepository.findTokenByToken("token");

        assertTrue(result.isPresent());
        assertFalse(result.get().isExpired());
        assertFalse(result.get().isRevoked());
    }

    @Test
    void logout_fail_because_wrong_token_type(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearr token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        logoutService.logout(request, response, null);
        var result = tokenRepository.findTokenByToken("token");

        assertTrue(result.isPresent());
        assertFalse(result.get().isExpired());
        assertFalse(result.get().isRevoked());
    }

    @Test
    void logout_fail_because_wrong_token(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer wrong");
        MockHttpServletResponse response = new MockHttpServletResponse();

        logoutService.logout(request, response, null);
        var result = tokenRepository.findTokenByToken("token");

        assertTrue(result.isPresent());
        assertFalse(result.get().isExpired());
        assertFalse(result.get().isRevoked());
    }
}
