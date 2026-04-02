package com.example.jwtDemo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.function.Function;

@Component
public class jwtUtil implements Serializable {


    public final String secretKey = "owiefewijeijeijofiwfijwjiwjjowiejjj" ;
    public String getUsernameFromToken(String token){
        return getClaimsFromToken(token, Claims::getSubject);
    }

    public String getExpirationTimeFromToken(String token){
        return getClaimsFromToken(token, Claims::getExpiration);
    }

    public <T> T  getClaimsFromToken(String token , Function<Claims,T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);


    }

    public Claims getAllClaimsFromToken(String token){
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }


}
