package com.example.restwithspringbootandjava.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MyFileNotFoundException extends RuntimeException {
    
    public MyFileNotFoundException(String ex){
        super(ex);
    }
    
    public MyFileNotFoundException(String ex, Throwable cause){
        super(ex, cause);
    }

}
