package com.example.jwt.filter;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class JwtHelper {

    private final JwtSecurityProperties jwtSecurityProperties;

    public JwtHelper(JwtSecurityProperties jwtSecurityProperties) {
        this.jwtSecurityProperties = jwtSecurityProperties;
    }

    public String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new JwtException("missing jwt token");
        }
        Jwts.parser().setSigningKey(jwtSecurityProperties.getToken().getSecret()).parseClaimsJws(token);
        return true;
    }

    private String getUsername(String token) {
        return Jwts.parser().setSigningKey(jwtSecurityProperties.getToken().getSecret()).parseClaimsJws(token).getBody().getSubject();
    }

    public Authentication getAuthentication(String token) {
        return new UsernamePasswordAuthenticationToken(getUsername(token), token, null);
    }
}