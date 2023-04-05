package com.nicosteinmueller.notes_app.Security.config;

import com.nicosteinmueller.notes_app.Models.Language;
import com.nicosteinmueller.notes_app.Models.Role;
import com.nicosteinmueller.notes_app.Models.Settings;
import com.nicosteinmueller.notes_app.Models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @BeforeEach
    void setUpEach() throws NoSuchMethodException {

    }
    @Test
    void extractUsername() {
        String token = Jwts
                .builder()
                .setClaims(new HashMap<String, Object>())
                .setSubject("username")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ 1000 * 60 * 10))
                .signWith(SIGN_KEY, SignatureAlgorithm.HS512)
                .compact();

        var result = jwtService.extractUsername(token);
        assertEquals(result, "username");
    }

    @Test
    void isTokenValid() {
        User user = new User("123", "first", "last", "username", "passwordHash",
                LocalDateTime.of(2023, 3, 20, 12, 0), new Settings(true, Language.ENGLISH), Role.USER);
        String token = Jwts
                .builder()
                .setClaims(new HashMap<String, Object>())
                .setSubject("username")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ 1000 * 60 * 10))
                .signWith(SIGN_KEY, SignatureAlgorithm.HS512)
                .compact();

        var result = jwtService.isTokenValid(token, user);
        assertTrue(result);
    }

    @Test
    void generateToken() {
    }

    @Test
    void testGenerateToken() {
    }

    @Test
    void testExtractUsername() {
    }

    @Test
    void extractClaim() {
    }
}