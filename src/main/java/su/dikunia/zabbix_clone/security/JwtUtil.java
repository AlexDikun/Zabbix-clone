package su.dikunia.zabbix_clone.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final String SECRET_KEY;

    public JwtUtil() {
        this.SECRET_KEY = System.getProperty("jwt.secret");
        if (SECRET_KEY == null) {
            throw new IllegalStateException("JWT secret key is not set in environment variables!");
        }
    }

    public String extractLogin(String token) { 
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateAccessToken(String login) { 
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, login, 1000 * 60 * 60 * 10); // 10 часов
    }

    public String generateRefreshToken(String login) { 
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, login, 1000 * 60 * 60 * 24 * 7); // 7 дней
    }

    private String createToken(Map<String, Object> claims, String subject, long expirationTime) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(SECRET_KEY))
                .compact();
    }

    public Boolean validateToken(String token, String login) {
        try {
            final String extractedLogin = extractLogin(token);
            return extractedLogin.equals(login) && !isTokenExpired(token);
        } catch (ExpiredJwtException e) {
            return false;
        }
    }

    Key getSigningKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }
}