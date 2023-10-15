package com.example.restwithspringbootandjava.dto.v1.security;

import java.io.Serializable;

import lombok.Data;

@Data
public class AccountCredentialsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

}
