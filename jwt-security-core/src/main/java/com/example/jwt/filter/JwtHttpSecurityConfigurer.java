package com.example.jwt.filter;

import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtHttpSecurityConfigurer extends AbstractHttpConfigurer<JwtHttpSecurityConfigurer, HttpSecurity> {

    @Override
    public void configure(HttpSecurity http) {
        ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);

        JwtSecurityProperties jwtSecurityProperties = applicationContext.getBean(JwtSecurityProperties.class);

        http.addFilterBefore(new JwtTokenFilter(jwtSecurityProperties), UsernamePasswordAuthenticationFilter.class);
    }

    public static JwtHttpSecurityConfigurer jwt() {
        return new JwtHttpSecurityConfigurer();
    }

}
