package com.example.jwt.filter;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtHttpSecurityConfigurer extends AbstractHttpConfigurer<JwtHttpSecurityConfigurer, HttpSecurity> {

    private JwtSecurityProperties jwtSecurityProperties;

    public JwtHttpSecurityConfigurer(JwtSecurityProperties jwtSecurityProperties) {
        this.jwtSecurityProperties = jwtSecurityProperties;
    }

    @Override
    public void init(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(HttpSecurity http) {
        http.addFilterBefore(new JwtTokenFilter(jwtSecurityProperties), UsernamePasswordAuthenticationFilter.class);
    }

    public static JwtHttpSecurityConfigurer jwt(JwtSecurityProperties jwtSecurityProperties) {
        return new JwtHttpSecurityConfigurer(jwtSecurityProperties);
    }

}
