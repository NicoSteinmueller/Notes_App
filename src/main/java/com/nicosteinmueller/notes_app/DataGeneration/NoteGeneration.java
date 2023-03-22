package com.nicosteinmueller.notes_app.DataGeneration;

import com.nicosteinmueller.notes_app.Models.Note;
import com.nicosteinmueller.notes_app.Models.User;

import java.time.LocalDateTime;
import java.util.List;

public class NoteGeneration {
    public static Note generateNote(User user) {
        return new Note(
                user,
                "Test",
                "Ich teste Notes.",
                List.of("Test"),
                LocalDateTime.now(),
                LocalDateTime.now(),
                false
        );
    }
}