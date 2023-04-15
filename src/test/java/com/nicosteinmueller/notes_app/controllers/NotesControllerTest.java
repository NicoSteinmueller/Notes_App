package com.nicosteinmueller.notes_app.controllers;

import com.nicosteinmueller.notes_app.Controllers.NotesController;
import com.nicosteinmueller.notes_app.Models.*;
import com.nicosteinmueller.notes_app.Models.api.NoteApi;
import com.nicosteinmueller.notes_app.Models.api.NoteShortApi;
import com.nicosteinmueller.notes_app.Repositorys.NoteRepository;
import com.nicosteinmueller.notes_app.Repositorys.UserRepository;
import com.nicosteinmueller.notes_app.Security.config.JwtService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class NotesControllerTest {
    @Mock
    UserRepository userRepository;
    @Autowired
    NoteRepository noteRepository;
    @Autowired
    MongoTemplate mongoTemplate;
    @Mock
    JwtService jwtService;
    @InjectMocks
    NotesController notesController;

    @BeforeEach
    void setUp(){
        notesController = new NotesController(jwtService, userRepository, noteRepository);
        when(jwtService.extractUsername(Mockito.anyMap())).thenReturn("user@mail");

        User user = new User("123", "first", "last", "user@mail", "passHash",
                LocalDateTime.of(2023, 3, 20, 12, 0), new Settings(true, Language.ENGLISH), Role.USER);
        when(userRepository.getUserByEmail(Mockito.any())).thenReturn(user);
        Note note = new Note(user, "title", "text", LocalDateTime.of(2023, 12, 1, 12, 0), LocalDateTime.of(2023, 12, 1, 12, 0), false);
        note.setId("123");
        noteRepository.save(note);
    }

    @AfterEach
    void cleanDatabase(){
        mongoTemplate.getDb().drop();
    }


    @Test
    void getNoteSuccess(){
        var response = notesController.getNote(new HashMap<>(), "123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        var respNote = response.getBody();
        assert respNote != null;
        assertEquals("title", respNote.getTitle());
    }

    @Test
    void getNoteFailBecauseNoteNotFound(){
        var response = notesController.getNote(new HashMap<>(), "456");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void saveNoteNewNoteSuccess(){
        var note = new NoteApi("", "test", "testText", LocalDateTime.of(2023, 3, 3, 12, 0), LocalDateTime.of(2023, 3, 3, 13, 0), false );
        var response = notesController.saveNote(new HashMap<>(), note);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        var respNote = response.getBody();
        assert respNote != null;
        assertEquals(note.getTitle(), respNote.getTitle());
        assertEquals(note.getText(), respNote.getText());
        assertEquals(note.isFavorite(), respNote.isFavorite());
    }
    @Test
    void saveNoteExistingNoteSuccess(){
        var note = new NoteApi("123", "test", "testText", LocalDateTime.of(2023, 3, 3, 12, 0), LocalDateTime.of(2023, 3, 3, 13, 0), false );
        var response = notesController.saveNote(new HashMap<>(), note);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        var respNote = response.getBody();
        assert respNote != null;
        assertEquals(note.getId(), respNote.getId());
        assertEquals(note.getTitle(), respNote.getTitle());
        assertEquals(note.getText(), respNote.getText());
        assertEquals(note.isFavorite(), respNote.isFavorite());
    }

    @Test
    void setNoteFavoriteSuccess(){
        var request = new NoteShortApi("123", "title", LocalDateTime.of(2024, 12, 1, 12, 0), true);
        var response = notesController.setNoteFavorite(new HashMap<>(), request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        var respNote = response.getBody();
        assert respNote != null;
        assertEquals(request.isFavorite(), respNote.isFavorite());
    }

    @Test
    void setNoteFavoriteFailBecauseNoteNotExists(){
        var request = new NoteShortApi("456", "title", LocalDateTime.of(2024, 12, 1, 12, 0), true);
        var response = notesController.setNoteFavorite(new HashMap<>(), request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAllNotesShortSuccess(){
        var response = notesController.getAllNotesShort(new HashMap<>());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        var resp = response.getBody();
        assert resp != null;
        assertEquals(1, resp.size());
        assertEquals("123", resp.get(0).getId());
    }

    @Test
    void deleteNoteSuccess(){
        var response = notesController.deleteNote(new HashMap<>(), "123");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteNoteFailBecauseNoteNotExists(){
        var response = notesController.deleteNote(new HashMap<>(), "456");
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
    }


}