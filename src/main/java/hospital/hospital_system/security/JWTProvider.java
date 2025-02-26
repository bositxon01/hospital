package hospital.hospital_system.security;

import hospital.hospital_system.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTProvider {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expirationDate}")
    private Integer expirationDate;

    private final static int ONE_DAY_IN_MILLISECONDS = 86_400_000;

    protected static final String PERMISSIONS = "permissions";

    public String generateToken(User user) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

        List<String> permissions = user.getAuthorities()
                .stream()
                .map(Object::toString)
                .collect(Collectors.toList());

        return Jwts.builder()
                .signWith(key)
                .setSubject(user.getUsername())
                .claim(PERMISSIONS, permissions) // Permissions are added to token
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationDate * ONE_DAY_IN_MILLISECONDS))
                .compact();
    }

    public Claims extractClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public List<String> extractPermissions(String token) {
        Claims claims = extractClaims(token);

        Object permissionsObject = claims.get(PERMISSIONS);

        if (permissionsObject instanceof List<?>) {
            return ((List<?>) permissionsObject).stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }

        return List.of();
    }

    public String validateToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}