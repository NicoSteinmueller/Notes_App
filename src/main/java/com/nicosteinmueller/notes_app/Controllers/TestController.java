package com.nicosteinmueller.notes_app.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Test", description = "Test if credentials valid.")
@SecurityRequirement(name = "Bearer Token")
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    @Operation(summary = "Test the user credentials.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New User is registered.", content = {@Content(mediaType = "application/string", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "Hello World!"))}),
            @ApiResponse(responseCode = "401", description = "The Token is invalid.", content = {@Content(mediaType = "application/string", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "JWT invalid. \n  OR\nJWT expired."))})
    })
    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello World!");
    }

}
