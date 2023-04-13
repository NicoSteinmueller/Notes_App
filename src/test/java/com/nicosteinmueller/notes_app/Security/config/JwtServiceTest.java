package com.nicosteinmueller.notes_app.Security.config;

import com.nicosteinmueller.notes_app.Models.Language;
import com.nicosteinmueller.notes_app.Models.Role;
import com.nicosteinmueller.notes_app.Models.Settings;
import com.nicosteinmueller.notes_app.Models.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtServiceTest {
    @InjectMocks
    JwtService jwtService;
    private static String SECRET_KEY = "dsgvbfdskjshfhj32745683245z4zJKHDSTUZFGHJASGFi3247895z7684325zuhjOIHZEWUDFTWDGESZUJFHGWEUDIF48237zt568473HUHGEFWUZFGWEhzuhewuzdgzuef"; //Same as application.yml
    private static Key SIGN_KEY;

    @BeforeAll
    static void setup(){
        SIGN_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }

    @Test
    void extractUsernameFromToken() {
        String token = Jwts
                .builder()
                .setClaims(new HashMap<>())
                .setSubject("username")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ 1000 * 60 * 10))
                .signWith(SIGN_KEY, SignatureAlgorithm.HS512)
                .compact();

        var result = jwtService.extractUsername(token);
        assertEquals(result, "username");
    }

    @Test()
    void isTokenValidWithValidToken() {
        User user = new User("123", "first", "last", "username", "passwordHash",
                LocalDateTime.of(2023, 3, 20, 12, 0), new Settings(true, Language.ENGLISH), Role.USER);
        String token = Jwts
                .builder()
                .setClaims(new HashMap<>())
                .setSubject("username")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ 1000 * 60 * 10))
                .signWith(SIGN_KEY, SignatureAlgorithm.HS512)
                .compact();


        var result = jwtService.isTokenValid(token, user);
        assertTrue(result);
    }

    @Test
    void isTokenValidWithExpiredToken() {
        User user = new User("123", "first", "last", "username", "passwordHash",
                LocalDateTime.of(2023, 3, 20, 12, 0), new Settings(true, Language.ENGLISH), Role.USER);
        String token = Jwts
                .builder()
                .setClaims(new HashMap<>())
                .setSubject("username")
                .setIssuedAt(new Date(System.currentTimeMillis()-1000))
                .setExpiration(new Date(System.currentTimeMillis()- 1))
                .signWith(SIGN_KEY, SignatureAlgorithm.HS512)
                .compact();

        assertThrows(ExpiredJwtException.class, () -> jwtService.isTokenValid(token, user));
    }

    @Test()
    void isTokenValidWithWrongUsername() {
        User user = new User("123", "first", "last", "username", "passwordHash",
                LocalDateTime.of(2023, 3, 20, 12, 0), new Settings(true, Language.ENGLISH), Role.USER);
        String token = Jwts
                .builder()
                .setClaims(new HashMap<>())
                .setSubject("wrongUsername")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ 1000 * 60 * 10))
                .signWith(SIGN_KEY, SignatureAlgorithm.HS512)
                .compact();


        var result = jwtService.isTokenValid(token, user);
        assertFalse(result);
    }

    @Test
    void generateToken(){
        User user = new User("123", "first", "last", "username", "passwordHash",
                LocalDateTime.of(2023, 3, 20, 12, 0), new Settings(true, Language.ENGLISH), Role.USER);

        String token = jwtService.generateToken(user);

        System.out.println(token.length());
        assertTrue(token.startsWith("eyJhbGciOiJIUzUxMiJ9."));
        assertEquals(178, token.length());
    }

    @Test
    void extractUsernameFromHeaders() {
        String token = Jwts
                .builder()
                .setClaims(new HashMap<>())
                .setSubject("username")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ 1000 * 60 * 10))
                .signWith(SIGN_KEY, SignatureAlgorithm.HS512)
                .compact();

        token = "Bearer: " + token;

        var headers = new TreeMap<String, String>();
        headers.put("authorization", token);

        var result = jwtService.extractUsername(headers);
        assertEquals(result, "username");
    }

}