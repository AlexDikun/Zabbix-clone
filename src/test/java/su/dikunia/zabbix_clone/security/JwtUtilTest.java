package su.dikunia.zabbix_clone.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import su.dikunia.zabbix_clone.domain.UserEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    private JwtUtil jwtUtil;
    private String secretKey = "my_secret_key_here_with_at_least_256_bits"; 
    private String login = "testuser";
    private String validToken;
    private String expiredToken;
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        System.setProperty("jwt.secret", secretKey);

        jwtUtil = new JwtUtil();

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_STAFF");
        userDetails = new org.springframework.security.core.userdetails.User(
                    login, "password", Collections.singletonList(authority));

        Key signingKey = jwtUtil.getSigningKey(secretKey);

        Map<String, Object> claims = new HashMap<>();
        validToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(login)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 часов
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        expiredToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(login)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 60)) // Истекший токен
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Test
    public void testExtractLogin() {
        String extractedLogin = jwtUtil.extractLogin(validToken);
        assertEquals(login, extractedLogin);
    }

    @Test
    public void testGenerateAccessToken() {
        String generatedToken = jwtUtil.generateAccessToken(userDetails);
        assertNotNull(generatedToken);
        assertTrue(jwtUtil.validateToken(generatedToken, login));
    }

    @Test
    public void testValidateToken() {
        boolean isValid = jwtUtil.validateToken(validToken, login);
        assertTrue(isValid);
    }

    @Test
    public void testExpiredToken() {
        boolean isValid = jwtUtil.validateToken(expiredToken, login);
        assertFalse(isValid);
    }
}