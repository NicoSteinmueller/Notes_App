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

@SpringBootApplication
public class NotesAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotesAppApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(UserRepository userRepository, NoteRepository noteRepository){
		return args -> {
			User user = UserGeneration.generateUser();
			userRepository.findUserByEmail(user.getEmail()).ifPresentOrElse(s -> {
				System.out.println(s.getEmail() + " already exists");
			}, () -> {
				System.out.println(user.getEmail() + " inserted");
				userRepository.insert(user);

				Note note = NoteGeneration.generateNote(user);
				noteRepository.insert(note);
			});
		};
	}
}
