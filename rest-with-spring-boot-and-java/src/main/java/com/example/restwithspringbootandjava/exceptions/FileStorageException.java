package com.example.restwithspringbootandjava.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FileStorageException extends RuntimeException {
    
    public FileStorageException(String ex){
        super(ex);
    }
    
    public FileStorageException(String ex, Throwable cause){
        super(ex, cause);
    }

}
