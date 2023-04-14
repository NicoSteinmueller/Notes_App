package com.nicosteinmueller.notes_app.Controllers;

import com.nicosteinmueller.notes_app.Models.User;
import com.nicosteinmueller.notes_app.Models.api.PasswordChange;
import com.nicosteinmueller.notes_app.Models.api.UserApi;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "User", description = "User management APIs")
@SecurityRequirement(name = "Bearer Token")
@RestController
@RequestMapping("${app.api.version}"+"/user")
@RequiredArgsConstructor
public class UserController {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "Update a User.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "User is updated.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples =@ExampleObject(value = "max@mustermann.de") )}),
            @ApiResponse(responseCode = "401", description = "The Token is invalid.", content = {@Content(mediaType = "application/string", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "JWT invalid. \n  OR\nJWT expired."))}),
            @ApiResponse(responseCode = "404", description = "User not found.", content = @Content),
            @ApiResponse(responseCode = "409", description = "User update not possible.", content = @Content)
    })
    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestHeader Map<String, String> headers, @RequestBody UserApi userApi){
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

    @Operation(summary = "Get a User.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return User.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserApi.class), examples = @ExampleObject(value = """
                    {
                        "firstName": "first",
                        "lastName": "last",
                        "email": "new@mail",
                        "settings": {
                            "language": "GERMAN",
                            "darkModeOn": true
                        }
                    }"""))}),
            @ApiResponse(responseCode = "401", description = "The Token is invalid.", content = {@Content(mediaType = "application/string", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "JWT invalid. \n  OR\nJWT expired."))}),
            @ApiResponse(responseCode = "404", description = "User not found.", content = @Content)
    })
    @GetMapping("/get")
    public ResponseEntity<UserApi> get(@RequestHeader Map<String, String> headers) {
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

    @Operation(summary = "Change Password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed.", content = @Content),
            @ApiResponse(responseCode = "401", description = "The Token is invalid.", content = {@Content(mediaType = "application/string", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "JWT invalid. \n  OR\nJWT expired."))}),
            @ApiResponse(responseCode = "404", description = "User not found.", content = @Content),
            @ApiResponse(responseCode = "409", description = "Old Password don't match.", content = @Content)
    })
    @PutMapping("/changePassword")
    public ResponseEntity<Object> changePassword(@RequestHeader Map<String, String> headers, @RequestBody PasswordChange passwordChange) {
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
