package com.nicosteinmueller.notes_app.controllers;

import com.nicosteinmueller.notes_app.Controllers.AuthenticationController;
import com.nicosteinmueller.notes_app.Models.Language;
import com.nicosteinmueller.notes_app.Models.Role;
import com.nicosteinmueller.notes_app.Models.Settings;
import com.nicosteinmueller.notes_app.Models.User;
import com.nicosteinmueller.notes_app.Repositorys.UserRepository;
import com.nicosteinmueller.notes_app.Security.auth.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
    @Mock
    AuthenticationService authenticationService;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    AuthenticationController authenticationController;

    @BeforeEach
    void setUp(){
        Settings settings = new Settings(true, Language.ENGLISH);
        User user = new User("123", "last", "first", "first@last.net", "passwordHash",
                LocalDateTime.of(2023,03,20,12,00, 00), settings, Role.USER);


    }
}
