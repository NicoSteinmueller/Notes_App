package com.nicosteinmueller.notes_app.controllers;

import com.nicosteinmueller.notes_app.Controllers.UserController;
import com.nicosteinmueller.notes_app.Repositorys.UserRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserController userController;


}
