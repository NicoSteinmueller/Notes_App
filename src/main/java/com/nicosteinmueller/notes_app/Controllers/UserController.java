package com.nicosteinmueller.notes_app.Controllers;

import com.nicosteinmueller.notes_app.Models.User;
import com.nicosteinmueller.notes_app.Models.api.PasswordChange;
import com.nicosteinmueller.notes_app.Models.api.UserApi;
import com.nicosteinmueller.notes_app.Repositorys.UserRepository;
import com.nicosteinmueller.notes_app.Security.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("${app.api.version}"+"/user")
@RequiredArgsConstructor
public class UserController {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PutMapping("/update")
    private ResponseEntity<String> update(@RequestHeader Map<String, String> headers, @RequestBody UserApi userApi){
        String usermail = jwtService.extractUsername(headers);
        var OptionalUser = userRepository.findUserByEmail(usermail);
        if (OptionalUser.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User user = OptionalUser.get();

        try {
            user.setFirstName(userApi.getFirstName());
            user.setLastName(userApi.getLastName());
            user.setEmail(userApi.getEmail());
            user.setSettings(userApi.getSettings());

            userRepository.save(user);
        }catch (Exception ignored) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(usermail);
    }

    @GetMapping("/get")
    private ResponseEntity<UserApi> get(@RequestHeader Map<String, String> headers) {
        String usermail = jwtService.extractUsername(headers);
        var OptionalUser = userRepository.findUserByEmail(usermail);
        if (OptionalUser.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        User user = OptionalUser.get();
        UserApi userApi = new UserApi(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getSettings());

        return new ResponseEntity<>(userApi, HttpStatus.OK);
    }

    @PutMapping("/changePassword")
    private ResponseEntity<Object> changePassword(@RequestHeader Map<String, String> headers, @RequestBody PasswordChange passwordChange) {
        String usermail = jwtService.extractUsername(headers);
        var OptionalUser = userRepository.findUserByEmail(usermail);
        if (OptionalUser.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        User user = OptionalUser.get();
        if (!passwordEncoder.matches(passwordChange.getOldPassword(), user.getPassword()))
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        var newPasswordEncoded = passwordEncoder.encode(passwordChange.getNewPassword());
        user.setPassword(newPasswordEncoded);
        userRepository.save(user);

        return  ResponseEntity.status(HttpStatus.OK).build();


    }

}
