package com.example.restwithspringbootandjava.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidJwtAuthenticationException extends AuthenticationException {
    
    public InvalidJwtAuthenticationException(String ex){
        super(ex);
    }

}
