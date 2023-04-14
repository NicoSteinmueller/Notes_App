package com.nicosteinmueller.notes_app.controllers;

import com.nicosteinmueller.notes_app.Controllers.UserController;
import com.nicosteinmueller.notes_app.Models.Language;
import com.nicosteinmueller.notes_app.Models.Role;
import com.nicosteinmueller.notes_app.Models.Settings;
import com.nicosteinmueller.notes_app.Models.User;
import com.nicosteinmueller.notes_app.Models.api.PasswordChange;
import com.nicosteinmueller.notes_app.Models.api.UserApi;
import com.nicosteinmueller.notes_app.Repositorys.UserRepository;
import com.nicosteinmueller.notes_app.Security.config.JwtService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    MongoTemplate mongoTemplate;
    @Mock
    JwtService jwtService;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserController userController;

    @BeforeEach
    void setUp(){
        userController = new UserController(jwtService, userRepository, passwordEncoder);
        when(jwtService.extractUsername(Mockito.anyMap())).thenReturn("user@mail");
        when(passwordEncoder.encode(Mockito.anyString())).thenReturn("passEncoded");
    }

    @AfterEach
    void cleanUpDatabase(){
        mongoTemplate.getDb().drop();
    }


    @Test
    void changePasswordSuccess(){
        User user = new User("123", "first", "last", "user@mail", "passHash",
                LocalDateTime.of(2023, 3, 20, 12, 0), new Settings(true, Language.ENGLISH), Role.USER);
        userRepository.save(user);
        var request = PasswordChange.builder().oldPassword("passHash").newPassword("newPass").build();
        when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(true);

        var response = userController.changePassword(new HashMap<>(), request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        var respUser = userRepository.getUserByEmail(user.getEmail());
        assertEquals("passEncoded", respUser.getPassword());
    }

    @Test
    void changePasswordFailBecauseInvalidOldPassword(){
        User user = new User("123", "first", "last", "user@mail", "passHash",
                LocalDateTime.of(2023, 3, 20, 12, 0), new Settings(true, Language.ENGLISH), Role.USER);
        userRepository.save(user);
        var request = PasswordChange.builder().oldPassword("wrongPass").newPassword("newPass").build();
        when(passwordEncoder.matches(Mockito.any(), Mockito.anyString())).thenReturn(false);

        var response = userController.changePassword(new HashMap<>(), request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        var respUser = userRepository.getUserByEmail(user.getEmail());
        assertEquals("passHash", respUser.getPassword());
    }

    @Test
    void changePasswordFailBecauseInvalidUserName(){
        User user = new User("123", "first", "last", "wrong@mail", "passHash",
                LocalDateTime.of(2023, 3, 20, 12, 0), new Settings(true, Language.ENGLISH), Role.USER);
        userRepository.save(user);
        var request = PasswordChange.builder().oldPassword("passHash").newPassword("newPass").build();
        when(passwordEncoder.matches(Mockito.any(), Mockito.anyString())).thenReturn(true);

        var response = userController.changePassword(new HashMap<>(), request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getSuccess(){
        User user = new User("123", "first", "last", "user@mail", "passHash",
                LocalDateTime.of(2023, 3, 20, 12, 0), new Settings(true, Language.ENGLISH), Role.USER);
        userRepository.save(user);

        var response = userController.get(new HashMap<>());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        var respUser = response.getBody();
        assert respUser != null;
        assertEquals(user.getEmail(), respUser.getEmail());
        assertEquals(user.getFirstName(), respUser.getFirstName());
        assertEquals(user.getLastName(), respUser.getLastName());
        assertEquals(user.getSettings(), respUser.getSettings());
    }

    @Test
    void getFailBecauseUserNotFound(){
        User user = new User("123", "first", "last", "wrong@mail", "passHash",
                LocalDateTime.of(2023, 3, 20, 12, 0), new Settings(true, Language.ENGLISH), Role.USER);
        userRepository.save(user);

        var response = userController.get(new HashMap<>());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    void updateSuccess(){
        User user = new User("123", "first", "last", "user@mail", "passHash",
                LocalDateTime.of(2023, 3, 20, 12, 0), new Settings(true, Language.ENGLISH), Role.USER);
        userRepository.save(user);

        var requestUserApi = UserApi.builder()
                .email("new@mail")
                .firstName("newFirst")
                .lastName("newLast")
                .settings(new Settings(false, Language.GERMAN))
                .build();

        var response = userController.update(new HashMap<>(), requestUserApi);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());

        var newUser = userRepository.getUserByEmail(requestUserApi.getEmail());

        assertEquals(requestUserApi.getEmail(), newUser.getEmail());
        assertEquals(requestUserApi.getFirstName(), newUser.getFirstName());
        assertEquals(requestUserApi.getLastName(), newUser.getLastName());
        assertEquals(requestUserApi.getSettings(), newUser.getSettings());
    }

    @Test
    void updateFailBecauseUserNotFound(){
        User user = new User("123", "first", "last", "wrong@mail", "passHash",
                LocalDateTime.of(2023, 3, 20, 12, 0), new Settings(true, Language.ENGLISH), Role.USER);
        userRepository.save(user);

        var requestUserApi = UserApi.builder()
                .email("new@mail")
                .firstName("newFirst")
                .lastName("newLast")
                .settings(new Settings(false, Language.GERMAN))
                .build();

        var response = userController.update(new HashMap<>(), requestUserApi);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateFailBecauseUserWithNewMailExistsAlready(){
        User user = new User("123", "first", "last", "user@mail", "passHash",
                LocalDateTime.of(2023, 3, 20, 12, 0), new Settings(true, Language.ENGLISH), Role.USER);
        userRepository.save(user);
        User user2 = new User("1234", "first", "last", "new@mail", "passHash",
                LocalDateTime.of(2023, 3, 20, 12, 0), new Settings(true, Language.ENGLISH), Role.USER);
        userRepository.save(user2);

        var requestUserApi = UserApi.builder()
                .email("new@mail")
                .firstName("newFirst")
                .lastName("newLast")
                .settings(new Settings(false, Language.GERMAN))
                .build();

        var response = userController.update(new HashMap<>(), requestUserApi);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

}
