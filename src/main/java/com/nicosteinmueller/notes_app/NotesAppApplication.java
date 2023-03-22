package com.nicosteinmueller.notes_app;

import com.nicosteinmueller.notes_app.DataGeneration.NoteGeneration;
import com.nicosteinmueller.notes_app.DataGeneration.UserGeneration;
import com.nicosteinmueller.notes_app.Models.Note;
import com.nicosteinmueller.notes_app.Models.User;
import com.nicosteinmueller.notes_app.Repositorys.NoteRepository;
import com.nicosteinmueller.notes_app.Repositorys.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class NotesAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotesAppApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(UserRepository userRepository, NoteRepository noteRepository){
		return args -> {
			//User user = UserGeneration.generateUser();

			for (User user:UserGeneration.generateUsers()
				 ) {
				userRepository.findUserByEmail(user.getEmail()).ifPresentOrElse(s -> {
					System.out.println(s.getEmail() + " already exists");
				}, () -> {
					System.out.println(user.getEmail() + " inserted");
					userRepository.insert(user);

					List<Note> note;

					switch (user.getEmail()) {
						case "amanda.tobes@avid.com":
							note = NoteGeneration.generateNotesAmanda(user);
							noteRepository.insert(note);
							break;
						case "tom.meier@avid.com":
							note = NoteGeneration.generateNotesTom(user);
							noteRepository.insert(note);
							break;
						case "a.klebrig@natgeo.de":
							note = NoteGeneration.generateNotesAndreas(user);
							noteRepository.insert(note);
							break;
						case "theelven@oracle.com":
							note = NoteGeneration.generateNotesAddaine(user);
							noteRepository.insert(note);
							break;
						case "itsstrange@marvel.com":
							note = NoteGeneration.generateNotesStrange(user);
							noteRepository.insert(note);
							break;

					}


				});

			}

		};
	}
}
