package com.nicosteinmueller.notes_app.DataGeneration;

import com.nicosteinmueller.notes_app.Models.Note;
import com.nicosteinmueller.notes_app.Models.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NoteGeneration {
    public static Note generateNote(User user) {
        return new Note(
                user,
                "Test",
                "Ich teste Notes.",
                List.of("Test"),
                LocalDateTime.now(),
                LocalDateTime.now(),
                false
        );
    }
    public static List<Note> generateNotesAmanda(User user){
        List<Note> list = new ArrayList<>();
        list.add(new Note(
                user,
                "Einkaufsliste",
                "Eier,Milch,Butter,Obst,TK-Pizza,Snacks,O-Saft",
                List.of("Shopping"),
                LocalDateTime.of(2023, 03, 22, 14, 33, 48, 0),
                LocalDateTime.of(2023, 03, 22, 17, 04, 45, 0),
                false
        ));
        list.add(new Note(
                user,
                "Termine",
                "Freitag: Besprechung Marketing, Besprechung Projektstatus Tom \n Montag: Entwicklungsgespräch Azubi \n Dienstag: Vorstellung Projektstatus",
                List.of("Work"),
                LocalDateTime.of(2016, 01, 12, 8, 43, 12, 0),
                LocalDateTime.of(2023, 03, 23, 15, 01, 34, 0),
                true
        ));


        return list;
    }


    public static List<Note> generateNotesTom(User user){
        List<Note> list = new ArrayList<>();
        list.add(new Note(
                user,
                "Projektbesprechung Amanda (Freitag)",
                "Absprache vor Präsi Dienstag, neue Änderungen vorstellen, Design-Idee abklären",
                List.of("Work"),
                LocalDateTime.of(2023, 03, 21, 22, 57, 00, 0),
                LocalDateTime.of(2023, 03, 21, 22, 57, 00, 0),
                false
        ));
        list.add(new Note(
                user,
                "Fußballspiel John",
                "Anstoß 14 Uhr, vorher John von der Schule abholen, Sportsachen hat er dabei",
                List.of("Football"),
                LocalDateTime.of(2023, 03, 22, 12, 22, 24, 0),
                LocalDateTime.of(2023, 03, 22, 12, 22, 30, 0),
                false
        ));
        list.add(new Note(
                user,
                "Hertha-Spiel Samstag",
                "Schal waschen, Robert abholen",
                List.of("Football"),
                LocalDateTime.of(2023, 03, 10, 23, 52, 21, 0),
                LocalDateTime.of(2023, 03, 12, 21, 14, 23, 0),
                false
        ));
        return list;
    }

    public static List<Note> generateNotesAndreas(User user){
        List<Note> list = new ArrayList<>();
        list.add(new Note(
                user,
                "Afrika-Reise",
                "Visum klären(erledigt), Hotel buchen, Leihwagen organisieren",
                List.of("Travel"),
                LocalDateTime.of(2023, 01, 22, 18, 34, 00, 0),
                LocalDateTime.of(2023, 03, 23, 10, 23, 55, 0),
                true
        ));
        list.add(new Note(
                user,
                "Foto-Wettbewerb: Spezies",
                "Panther, Tiger, Elephant, Giraffe, Löwe",
                List.of("Exploration"),
                LocalDateTime.of(2023, 01, 22, 18, 34, 00, 0),
                LocalDateTime.of(2023, 03, 23, 10, 23, 55, 0),
                false
        ));
        list.add(new Note(
                user,
                "Absprache Addaine",
                "Zeitplan abklären, Absprache bzgl. Hotel (Pool?)",
                List.of("Travel"),
                LocalDateTime.of(2023, 02, 12, 23, 17, 23, 0),
                LocalDateTime.of(2023, 02, 12, 23, 17, 23, 0),
                false
        ));
        return list;
    }

    public static List<Note> generateNotesAddaine(User user){
        List<Note> list = new ArrayList<>();
        list.add(new Note(
                user,
                "Absprache Andreas",
                "Zeitplan abklären, Absprache bzgl. Hotelkosten (Kein Pool!)",
                List.of("Travel"),
                LocalDateTime.of(2023, 02, 13, 13, 12, 23, 0),
                LocalDateTime.of(2023, 02, 13, 13, 13, 44, 0),
                false
        ));
        list.add(new Note(
                user,
                "Liste an zu fotografierenden Spezies (Fotowettbewerb)",
                "Panther, Tiger, Elephant, Giraffe, Löwe",
                List.of("Work"),
                LocalDateTime.of(2022, 12, 23, 21, 17, 23, 0),
                LocalDateTime.of(2023, 03, 14, 15, 1, 3, 0),
                true
        ));
        list.add(new Note(
                user,
                "Besprechung Leiter Galerie",
                "",
                List.of("Work"),
                LocalDateTime.of(2023, 02, 12, 14, 13, 37, 0),
                LocalDateTime.of(2023, 02, 12, 14, 13, 37, 0),
                false
        ));
        return list;
    }

    public static List<Note> generateNotesStrange(User user){
        List<Note> list = new ArrayList<>();
        list.add(new Note(
                user,
                "Besprechung weiteres Vorgehen Dormamu",
                "Verhandlungsbasis klären",
                List.of("Travel"),
                LocalDateTime.of(2019, 12, 31, 8, 12, 54, 0),
                LocalDateTime.of(2019, 12, 31, 8, 12, 54, 0),
                false
        ));
        list.add(new Note(
                user,
                "Fusballspiel Tom's Sohn",
                "Treffen 13:45 and Greenhouse-Arena",
                List.of("Football"),
                LocalDateTime.of(2023, 03, 24, 17, 44, 00, 0),
                LocalDateTime.of(2023, 03, 24, 17, 44, 32, 0),
                false
        ));
        list.add(new Note(
                user,
                "Hertha-Spiel Samstag",
                "Treffen mit Tom 14:45",
                List.of("Football"),
                LocalDateTime.of(2023, 03, 24, 17, 48, 14, 0),
                LocalDateTime.of(2023, 03, 24, 17, 48, 59, 0),
                false
        ));
        return list;
    }

}

