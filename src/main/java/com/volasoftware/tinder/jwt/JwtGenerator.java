package com.volasoftware.tinder.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public class JwtGenerator {
    private static final String SECRET_KEY = "UnbreakableSecretKey";
    @Value("$jwt_expiration_time")
    private static int EXPIRATION_TIME;

    public static String generateToken(String username){
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
}
