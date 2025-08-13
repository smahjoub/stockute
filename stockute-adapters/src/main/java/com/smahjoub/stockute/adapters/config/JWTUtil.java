package com.smahjoub.stockute.adapters.config;

import com.smahjoub.stockute.adapters.restful.membership.dto.UserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JWTUtil {

    @Value("${springbootwebfluxjjwt.jjwt.secret}")
    private String secret;

    @Value("${springbootwebfluxjjwt.jjwt.expiration}")
    private String expirationTime;


    public Claims getAllClaimsFromToken(final String token) {
        final var key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

    public String getUsernameFromToken(final String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Date getExpirationDateFromToken(final  String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Boolean isTokenExpired(final  String token) {
        final var expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(final UserDTO user) {
        final Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.roles());
        claims.put("fullName", String.format("%s %s", user.firstName(), user.lastName()));
        claims.put("email", user.email());
        return doGenerateToken(claims, user.userName());
    }


    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }


    private String doGenerateToken(Map<String, Object> claims, String username) {
        final var expirationTimeLong = Long.parseLong(expirationTime); //in second
        final var createdDate = new Date();
        final var expirationDate = new Date(createdDate.getTime() + expirationTimeLong * 1000);
        final var key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(createdDate)
                .expiration(expirationDate)
                .signWith(key)
                .compact();
    }


}
