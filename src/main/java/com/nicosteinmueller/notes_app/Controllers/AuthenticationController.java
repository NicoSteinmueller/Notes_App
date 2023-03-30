package com.nicosteinmueller.notes_app.Controllers;

import com.nicosteinmueller.notes_app.Models.api.AuthenticationRequest;
import com.nicosteinmueller.notes_app.Models.api.AuthenticationResponse;
import com.nicosteinmueller.notes_app.Models.api.RegisterRequest;
import com.nicosteinmueller.notes_app.Repositorys.UserRepository;
import com.nicosteinmueller.notes_app.Security.auth.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Authentication management APIs")
@RestController
@RequestMapping("${app.api.version}"+"/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    @Operation(summary = "Register a new User.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New User is registered.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class))}),
            @ApiResponse(responseCode = "409", description = "The User already exists.", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register (@Parameter(description = "The Data of the User.") @RequestBody RegisterRequest request) {
        if (userRepository.findUserByEmail(request.getEmail()).isPresent())
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @Operation(summary = "Authenticate a User.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is authenticated.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class))}),
            @ApiResponse(responseCode = "403", description = "The User's credentials are invalid.", content = @Content)
    })
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate (@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

}
