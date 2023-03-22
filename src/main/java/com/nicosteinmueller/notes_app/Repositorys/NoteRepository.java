package com.nicosteinmueller.notes_app.Repositorys;

import com.nicosteinmueller.notes_app.Models.Note;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NoteRepository extends MongoRepository<Note, String> {
}
