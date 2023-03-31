package com.nicosteinmueller.notes_app.Models.api;

import com.nicosteinmueller.notes_app.Models.Note;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NoteShortApi {
    private String id;
    private String title;
    private LocalDateTime modified;
    private boolean favorite;

    public NoteShortApi(Note note){
        id = note.getId();
        title = note.getTitle();
        modified = note.getModified();
        favorite = note.isFavorite();
    }
}
