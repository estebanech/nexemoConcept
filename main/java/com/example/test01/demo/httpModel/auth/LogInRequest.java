package com.example.test01.demo.httpModel.auth;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LogInRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
