package com.nicosteinmueller.notes_app.Repositorys;

import com.nicosteinmueller.notes_app.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findUserByEmail(String email);
    User getUserByEmail(String email);
}
