package com.nicosteinmueller.notes_app.Models.api;

import com.nicosteinmueller.notes_app.Models.Note;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class NoteApi {
    private String id;
    private String title;
    private String text;
    private List<String> labels;
    private LocalDateTime created;
    private LocalDateTime modified;
    private boolean favorite;

    public NoteApi(Note note){
        id = note.getId();
        title = note.getTitle();
        text = note.getText();
        labels = note.getLabels();
        created = note.getCreated();
        modified = note.getModified();
        favorite = note.isFavorite();
    }
}
