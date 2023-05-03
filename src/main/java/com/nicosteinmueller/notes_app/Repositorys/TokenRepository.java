package com.nicosteinmueller.notes_app.Repositorys;

import com.nicosteinmueller.notes_app.Models.Token;
import com.nicosteinmueller.notes_app.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends MongoRepository<Token, String> {
    List<Token> findAllByUserAndExpiredFalseOrRevokedIsFalse(User user);
    Optional<Token> findTokenByToken (String token);
}
