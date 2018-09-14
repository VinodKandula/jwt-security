package com.example.jwt.filter;

import org.springframework.security.core.AuthenticationException;

public class MissingJwtTokenException extends AuthenticationException {

    public MissingJwtTokenException(String message) {
        super(message);
    }
}