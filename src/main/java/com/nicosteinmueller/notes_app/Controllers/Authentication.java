package com.nicosteinmueller.notes_app.Controllers;

import com.nicosteinmueller.notes_app.Models.User;
import com.nicosteinmueller.notes_app.Repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("${app.api.version}"+"/auth")
public class Authentication {
    @Autowired
    private UserRepository userRepository;
    @PostMapping("/test")
    public ResponseEntity<String> login (@RequestHeader Map<String, String> headers){
        headers.forEach((key, value) -> {
            System.out.println(key+" : "+value);
        });
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(headers.toString());
    }

    @PostMapping("/register")
    public ResponseEntity<Boolean> register(@RequestParam String email, @RequestParam String password) {
        try {
            userRepository.insert(new User(null, null,password, email, null, null, null));
            return new ResponseEntity<>(true, HttpStatus.ACCEPTED);
        } catch (Exception ignored){
            return new ResponseEntity<>(false, HttpStatus.CONFLICT);
        }
    }

}
