package es.mercadona.api_tiendas.config;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private String expiration;

    public String generateToken(String username) {
        return buildToken(username, expiration);
    }

    public String buildToken(String username, String expiration) {

        return Jwts.builder()
                .subject(username)
                .claim("roles", "ADMIN")
                .claim("scope", "READ,WRITE")
                .claim("client_id", "api_tiendas")
                .issuedAt(new Date())
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                .expiration(new Date(System.currentTimeMillis() + Long.parseLong(expiration)))
                .compact();

    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        final Claims jwtToken = Jwts.parser()
                .verifyWith(getSignInKey())
                .build().parseSignedClaims(token).getPayload();

        return jwtToken.getSubject();
    }

    public boolean isTokenExpired(String token) {
        final Date expiration = Jwts.parser()
                .verifyWith(getSignInKey())
                .build().parseSignedClaims(token).getPayload().getExpiration();
        return expiration.before(new Date());
    }

    public boolean isTokenValid(String token, String username) {
        final String usernameToken = extractUsername(token);
        return usernameToken.equals(username) && !isTokenExpired(token);
    }
}
