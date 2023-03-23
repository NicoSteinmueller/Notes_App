package com.nicosteinmueller.notes_app.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("${app.api.version}"+"/auth")
public class Authentication {
    @PostMapping("/test")
    public ResponseEntity<String> login (@RequestHeader Map<String, String> headers){
        headers.forEach((key, value) -> {
            System.out.println(key+" : "+value);
        });
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(headers.toString());
    }
}
