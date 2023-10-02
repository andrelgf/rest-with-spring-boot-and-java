package com.example.restwithspringbootandjava.dto.v1;

import java.io.Serializable;

import lombok.Data;

@Data
public class PersonDTO implements Serializable {
    
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
   
}
