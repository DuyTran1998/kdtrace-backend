package com.duytran.kdtrace.security.jwt;

import com.duytran.kdtrace.security.principal.UserPrincipal;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
@SuppressWarnings("FieldCanBeLocal")
public class JwtTokenProvider {
    //Private - just know on server.
    private final String JWT_SECRET = "kdtrace";

    private static final String AUTHORITIES_KEY = "auth";

    //Exp time token
    private final Long JWT_EXPRIRATION = 86400000L;

    //Create token from information.
    public String generateToken(Authentication authentication){
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        Date now = new Date();
        Date expDate = new Date(now.getTime() + JWT_EXPRIRATION);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .setExpiration(expDate)
                .compact();
    }

    //decode token to have id user.
    public String getUserNameFromJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    //validate token
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
}