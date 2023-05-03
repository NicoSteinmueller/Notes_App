package com.nicosteinmueller.notes_app.Models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;

@Data
@Document
public class Note {
    @Id
    private String id;
    @DocumentReference(lazy = true)
    private User user;
    private String title;
    private String text;
    private LocalDateTime created;
    private LocalDateTime modified;
    private boolean favorite;

    public Note(User user, String title, String text, LocalDateTime created, LocalDateTime modified, boolean favorite) {
        this.user = user;
        this.title = title;
        this.text = text;
        this.created = created;
        this.modified = modified;
        this.favorite = favorite;
    }
}
