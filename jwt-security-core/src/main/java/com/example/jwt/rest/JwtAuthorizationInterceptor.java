package com.example.jwt.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

public class JwtAuthorizationInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("--- Propagated Authentication: " + auth.getName() + " " + auth.getCredentials() + " " + auth.getAuthorities());
        if (auth != null && auth instanceof UsernamePasswordAuthenticationToken) {
            request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + auth.getCredentials());
        }
        return execution.execute(request, body);
    }
}
