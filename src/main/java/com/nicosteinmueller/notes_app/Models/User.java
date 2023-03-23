package com.nicosteinmueller.notes_app.Models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document
public class User {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String password;
    @Indexed(unique = true)
    private String email;
    private LocalDateTime created;
    private List<String> labels;
    private Settings settings;

    public User(String firstName, String lastName, String password, String email, LocalDateTime created, List<String> labels, Settings settings) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.created = created;
        this.labels = labels;
        this.settings = settings;
    }
}
