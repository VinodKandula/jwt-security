package com.example.jwt.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtTokenFilter extends OncePerRequestFilter {

    private JwtSecurityProperties jwtSecurityProperties;

    public JwtTokenFilter(JwtSecurityProperties jwtSecurityProperties) {
        this.jwtSecurityProperties = jwtSecurityProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = obtainToken(request);
        try {
            if (token == null) {
                throw new JwtException("missing jwt token");
            }
            Claims claims = obtainClaims(token);
            Authentication authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), "", getAuthorities(claims));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException e) {
            SecurityContextHolder.clearContext();
            //response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        chain.doFilter(request, response);
    }

    public String obtainToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    private Claims obtainClaims(String token) {
        return Jwts.parser().setSigningKey(jwtSecurityProperties.getToken().getSecret()).parseClaimsJws(token).getBody();
    }

    private List<GrantedAuthority> getAuthorities(Claims claims) {
        List<String> authorities = claims.get("authorities", List.class);
        return authorities.stream().map((role) -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
    }
}
