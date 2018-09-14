package com.example.jwt.filter;

import org.springframework.security.core.AuthenticationException;

public class MalformedJwtTokenException extends AuthenticationException {

    public MalformedJwtTokenException(String message) {
        super(message);
    }
}
