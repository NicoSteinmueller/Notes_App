package com.nicosteinmueller.notes_app.Models.api;

import com.nicosteinmueller.notes_app.Models.Note;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NoteApi {
    private String id;
    private String title;
    private String text;
    private LocalDateTime created;
    private LocalDateTime modified;
    private boolean favorite;

    public NoteApi(Note note){
        id = note.getId();
        title = note.getTitle();
        text = note.getText();
        created = note.getCreated();
        modified = note.getModified();
        favorite = note.isFavorite();
    }
}
