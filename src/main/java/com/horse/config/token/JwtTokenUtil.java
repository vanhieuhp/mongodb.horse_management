package com.horse.config.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.horse.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expirationDateInMs}")
    private int expirationDateInMs;

    @Value("${jwt.refreshExpirationDateInMs}")
    private int refreshExpirationDateInMs;

    public DecodedJWT getDecodedJwtFromToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        return jwtVerifier.verify(token);
    }

    public String getSubjectFromToken(String token) {
        return getDecodedJwtFromToken(token).getSubject();
    }

    public String generateAccessToken(String username, String authority) {
        return JWT.create().withSubject(username).withClaim("key", authority)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationDateInMs))
                .sign(Algorithm.HMAC256(secret.getBytes()));
    }

    public String generateRefreshToken(String username) {
        return JWT.create().withSubject(username).withClaim("key", "REFRESH_TOKEN")
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshExpirationDateInMs))
                .sign(Algorithm.HMAC256(secret.getBytes()));
    }

    public String getAuthoritiesFromToken(String token) {
        String role = null;
        try {
            role = getDecodedJwtFromToken(token).getClaim("key").as(String.class);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return role;
    }

    public boolean checkFreshToken(String token) {
        boolean check = false;
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            String claim = verifier.verify(token).getClaim("key").asString();
            if ("REFRESH_TOKEN".equals(claim)) {
                check = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return check;
    }

    public boolean validate(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
