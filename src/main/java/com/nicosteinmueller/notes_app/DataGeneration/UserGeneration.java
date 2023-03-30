package com.nicosteinmueller.notes_app.DataGeneration;


import com.nicosteinmueller.notes_app.Models.Language;
import com.nicosteinmueller.notes_app.Models.Settings;
import com.nicosteinmueller.notes_app.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserGeneration {
    @Autowired
    private PasswordEncoder passwordEncoder;
    public User generateUser(){
        Settings settings = new Settings(true, Language.GERMAN);
        return new User(
                "Max",
                "Mustermann",
                passwordEncoder.encode("test"),
                "max@mustermann.de",
                LocalDateTime.now(),
                settings);
    }
}
