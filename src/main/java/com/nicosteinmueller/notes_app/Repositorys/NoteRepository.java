package com.nicosteinmueller.notes_app.Repositorys;

import com.nicosteinmueller.notes_app.Models.Note;
import com.nicosteinmueller.notes_app.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends MongoRepository<Note, String> {
    Optional<Note> findNoteByIdAndUser (String id, User user);
    List<Note> getAllByUser (User user);
    void deleteNoteById (String id);
}
