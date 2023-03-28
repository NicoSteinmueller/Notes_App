package com.nicosteinmueller.notes_app.Models.api;

import com.nicosteinmueller.notes_app.Models.Note;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NoteShortApi {
    private String id;
    private String title;
    private List<String> labels;
    private LocalDateTime modified;
    private boolean favorite;

    public NoteShortApi(Note note){
        id = note.getId();
        title = note.getTitle();
        labels = note.getLabels();
        modified = note.getModified();
        favorite = note.isFavorite();
    }
}
