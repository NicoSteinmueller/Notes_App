package com.nicosteinmueller.notes_app.DataGeneration;

import com.nicosteinmueller.notes_app.Models.Language;
import com.nicosteinmueller.notes_app.Models.Settings;
import com.nicosteinmueller.notes_app.Models.User;
import com.nicosteinmueller.notes_app.Utilities.HashUtilities;

import java.time.LocalDateTime;
import java.util.List;

public class UserGeneration {
    public static User generateUser(){
        Settings settings = new Settings(true, Language.GERMAN);
        return new User(
                "Max",
                "Mustermann",
                HashUtilities.hashSHA512("password"),
                "max@mustermann.de",
                LocalDateTime.now(),
                List.of("Test", "Work"),
                settings);
    }
}
