package com.example.restwithspringbootandjava.exceptions;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExceptionResponse implements Serializable {

    private Date timestamp;
    private String message;
    private String details;
    
}
