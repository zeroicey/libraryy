package com.acacia.libraryy.utils;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

// use java-jwt
public class JwtUtil {
    public static String genToken(Integer userId) {
        return JWT.create()
                .withClaim("userId", userId)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000L))
                .sign(Algorithm.HMAC256("libraryy"));
    }

    public static Boolean verifyToken(String token) {
        try {
            JWT.require(Algorithm.HMAC256("libraryy")).build().verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Integer getUserId(String token) {
        return JWT.require(Algorithm.HMAC256("libraryy")).build().verify(token).getClaim("userId").asInt();
    }
}
