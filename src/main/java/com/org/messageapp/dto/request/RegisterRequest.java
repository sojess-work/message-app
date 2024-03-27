package com.org.messageapp.dto.request;

import lombok.Data;

@Data
public class RegisterRequest {

    private String email;
    private String password;
}
