package com.example.jwt.filter;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    private final JwtHelper jwtHelper;

    public JwtWebSecurityConfigurerAdapter(JwtHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .anyRequest().authenticated()
                .and()
                // There is no Set-Cookie header set like: Set-Cookie: JSESSIONID=FA8D9D63AC673210862275C8D36431E3; Path=/; HttpOnly
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().httpBasic()
                .and()
                .addFilterBefore(new JwtTokenFilter(jwtHelper), UsernamePasswordAuthenticationFilter.class);
    }

}
