package com.example.jwt.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

public class JwtTokenFilter extends GenericFilterBean  {

    private JwtSecurityProperties jwtSecurityProperties;

    public JwtTokenFilter(JwtSecurityProperties jwtSecurityProperties) {
        this.jwtSecurityProperties = jwtSecurityProperties;
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("--- I was called... " + Thread.currentThread().getName());
        String token = obtainToken((HttpServletRequest) request);
        try {
            if (token == null) {
                throw new JwtException("missing jwt token");
            }
            Claims claims = obtainClaims(token);
            Authentication authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), token, getAuthorities(claims));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException e) {
            ((HttpServletResponse) response).setStatus(SC_UNAUTHORIZED);
        }

        chain.doFilter(request, response);
        SecurityContextHolder.clearContext();
    }

    public String obtainToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        System.out.println("--- Authorization header:" + bearerToken);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
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
