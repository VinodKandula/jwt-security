package com.example.jwt.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class JwtTokenFilter extends OncePerRequestFilter {

    private JwtSecurityProperties jwtSecurityProperties;

    public JwtTokenFilter(JwtSecurityProperties jwtSecurityProperties) {
        this.jwtSecurityProperties = jwtSecurityProperties;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String token = obtainToken(request);

        if (token == null) {
            throw new MissingJwtTokenException("missing JWT token");
        }
        try {
            Claims claims = obtainClaims(token);
            Authentication authentication = createAuthentication(claims, token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException e) {
            throw new MalformedJwtTokenException("malformed JWT token");
        }

        filterChain.doFilter(request, response);
    }

    private String obtainToken(HttpServletRequest request) {
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

    private List<GrantedAuthority> getGrantedAuthorities(Claims claims) {
        return AuthorityUtils.commaSeparatedStringToAuthorityList(claims.get("authorities", String.class));
    }

    private Authentication createAuthentication(Claims claims, String token) {
        // we store the token , we need in the JwtAuthorizationInterceptor
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), token, getGrantedAuthorities(claims));
    }

}
