package com.nicosteinmueller.notes_app.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Data
@Document
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    @Id
    private String id;
    @Indexed(unique = true)
    public String token;
    public TokenType tokenType = TokenType.BEARER;
    public boolean revoked;
    public boolean expired;
    @DocumentReference(lazy = true)
    public User user;
}
