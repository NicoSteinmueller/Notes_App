package com.nicosteinmueller.notes_app.Controllers;

import com.nicosteinmueller.notes_app.Models.Note;
import com.nicosteinmueller.notes_app.Models.User;
import com.nicosteinmueller.notes_app.Models.api.NoteApi;
import com.nicosteinmueller.notes_app.Models.api.NoteShortApi;
import com.nicosteinmueller.notes_app.Repositorys.NoteRepository;
import com.nicosteinmueller.notes_app.Repositorys.UserRepository;
import com.nicosteinmueller.notes_app.Security.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("${app.api.version}"+"/notes")
@RequiredArgsConstructor
public class NotesController {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final NoteRepository noteRepository;

    @GetMapping("/getNote")
    public ResponseEntity<NoteApi> getNote (@RequestHeader Map<String, String> headers, @RequestParam String noteId){
        String userEmail = jwtService.extractUsername(headers);
        User user = userRepository.getUserByEmail(userEmail);
        var optionalNote = noteRepository.findNoteByIdAndUser(noteId, user);

        if (optionalNote.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        var noteCompleteApi = new NoteApi(optionalNote.get());

        return ResponseEntity.ok(noteCompleteApi);
    }

    @PostMapping("/saveNote")
    public ResponseEntity<NoteApi> saveNote(@RequestHeader Map<String, String> headers, @RequestBody NoteApi noteSave){
        String userEmail = jwtService.extractUsername(headers);
        User user = userRepository.getUserByEmail(userEmail);

        Note note;
        var optionalNote = noteRepository.findNoteByIdAndUser(noteSave.getId(), user);
        if (noteSave.getId().isBlank()|| optionalNote.isEmpty()){
            note = new Note(user, noteSave.getTitle(), noteSave.getText(),
                     LocalDateTime.now(), LocalDateTime.now(), noteSave.isFavorite());
            noteRepository.insert(note);
        }else {
            note = optionalNote.get();
            note.setTitle(noteSave.getTitle());
            note.setText(noteSave.getText());
            note.setFavorite(noteSave.isFavorite());
            noteRepository.save(note);
        }

        return ResponseEntity.ok(new NoteApi(note));
    }

    @GetMapping("/getAllNotesShort")
    public ResponseEntity<List<NoteShortApi>> getAllNotesShort(@RequestHeader Map<String, String> headers){
        String userEmail = jwtService.extractUsername(headers);
        User user = userRepository.getUserByEmail(userEmail);

        var notes = noteRepository.getAllByUser(user);
        List<NoteShortApi> notesShort = new ArrayList<>();
        notes.forEach(note -> notesShort.add(new NoteShortApi(note)));

        return ResponseEntity.ok(notesShort);
    }

}
