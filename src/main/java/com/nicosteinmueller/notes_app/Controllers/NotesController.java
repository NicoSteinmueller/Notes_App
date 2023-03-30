package com.nicosteinmueller.notes_app.Controllers;

import com.nicosteinmueller.notes_app.Models.Note;
import com.nicosteinmueller.notes_app.Models.User;
import com.nicosteinmueller.notes_app.Models.api.NoteApi;
import com.nicosteinmueller.notes_app.Models.api.NoteShortApi;
import com.nicosteinmueller.notes_app.Repositorys.NoteRepository;
import com.nicosteinmueller.notes_app.Repositorys.UserRepository;
import com.nicosteinmueller.notes_app.Security.config.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Tag(name = "Notes", description = "Notes management APIs")
@SecurityRequirement(name = "Bearer Token")
@RestController
@RequestMapping("${app.api.version}"+"/notes")
@RequiredArgsConstructor
public class NotesController {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final NoteRepository noteRepository;

    @Operation(summary = "Get a Note.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Return Note.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NoteApi.class), examples =@ExampleObject(value = """
                    {
                        "id": "6421ce0fe602881f58cc6184",
                        "title": "Test",
                        "text": "Ich teste Notes.",
                        "created": "2023-03-27T19:10:39.577",
                        "modified": "2023-03-27T19:10:39.577",
                        "favorite": false
                    }""") )}),
            @ApiResponse(responseCode = "401", description = "The Token is invalid.", content = {@Content(mediaType = "application/string", schema = @Schema(implementation = String.class), examples =
            @ExampleObject(value = "JWT invalid. \n  OR\nJWT expired."))}),
            @ApiResponse(responseCode = "404", description = "Note not found.", content = @Content)
    })
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

    @Operation(summary = "Save Note.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Note saved.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NoteApi.class), examples =@ExampleObject(value = """
                    {
                        "id": "6421ce0fe602881f58cc6184",
                        "title": "Test",
                        "text": "Ich teste Notes.",
                        "created": "2023-03-27T19:10:39.577",
                        "modified": "2023-03-27T19:10:39.577",
                        "favorite": false
                    }""") )}),
            @ApiResponse(responseCode = "401", description = "The Token is invalid.", content = {@Content(mediaType = "application/string", schema = @Schema(implementation = String.class), examples =
            @ExampleObject(value = "JWT invalid. \n  OR\nJWT expired."))})
    })
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

    @Operation(summary = "Set Note Favorite.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Note Favorite saved.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NoteShortApi.class), examples =@ExampleObject(value = """
                    {
                        "id": "6421ce0fe602881f58cc6184",
                        "title": "Test",
                        "modified": "2023-03-27T19:10:39.577",
                        "favorite": true
                    }""") )}),
            @ApiResponse(responseCode = "401", description = "The Token is invalid.", content = {@Content(mediaType = "application/string", schema = @Schema(implementation = String.class), examples =
            @ExampleObject(value = "JWT invalid. \n  OR\nJWT expired."))}),
            @ApiResponse(responseCode = "404", description = "Note not found.", content = @Content)
    })
    @PostMapping("/setNoteFavorite")
    public ResponseEntity<NoteShortApi> setNoteFavorite(@RequestHeader Map<String, String> headers, @RequestBody NoteShortApi noteShort){
        String userEmail = jwtService.extractUsername(headers);
        User user = userRepository.getUserByEmail(userEmail);

        Note note;
        var optionalNote = noteRepository.findNoteByIdAndUser(noteShort.getId(), user);
        if (optionalNote.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else {
            note = optionalNote.get();
            note.setFavorite(noteShort.isFavorite());
            noteRepository.save(note);
        }

        return ResponseEntity.ok(new NoteShortApi(note));
    }

    @Operation(summary = "Get all Notes short.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Return all Notes short.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NoteShortApi.class), examples =@ExampleObject(value = """
                    [
                        {
                            "id": "6421ce0fe602881f58cc6184",
                            "title": "Test",
                            "modified": "2023-03-27T19:10:39.577",
                            "favorite": false
                        },
                        {
                            "id": "64230c3e25a2555fcfb11eb0",
                            "title": "Test",
                            "modified": "2023-03-28T17:48:01.374",
                            "favorite": false
                        }
                    ]""") )}),
            @ApiResponse(responseCode = "401", description = "The Token is invalid.", content = {@Content(mediaType = "application/string", schema = @Schema(implementation = String.class), examples =
            @ExampleObject(value = "JWT invalid. \n  OR\nJWT expired."))})
    })
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
