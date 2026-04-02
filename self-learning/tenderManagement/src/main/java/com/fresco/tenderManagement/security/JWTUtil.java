package com.fresco.tenderManagement.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtil implements Serializable {

    private static final long serialVersionUID = 654352132132L;
    public static final long JWT_TOKEN_VALIDITY = 500 * 60 * 60; // token valid for 500 hours in milliseconds
    private final String secretKey = "randomKey123";

    // extracts email (stored as subject) from the token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // extracts expiry date from the token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // generic helper — applies any function to the claims object
    // e.g. Claims::getSubject extracts the subject field
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        return claimsResolver.apply(claims);
    }

//    // parses the token using the secret key and returns all claims
//    private Claims getAllClaimsFromToken(String token) {
//        return Jwts.parser()
//                .setSigningKey(secretKey)
//                .parseClaimsJws(token)
//                .getBody();
//    }

    // returns true if token expiry date is before right now
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // called from LoginController after successful authentication
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        String subject = userDetails.getUsername();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();

        //return doGenerateToken(claims, userDetails.getUsername()); // username = email
    }

//    // builds and signs the JWT string
//    private String doGenerateToken(Map<String, Object> claims, String subject) {
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(subject)                       // email goes here
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
//                .signWith(SignatureAlgorithm.HS512, secretKey)
//                .compact();
//    }

    // checks: email in token matches the user AND token is not expired
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}