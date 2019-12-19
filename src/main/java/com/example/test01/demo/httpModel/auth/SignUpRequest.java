package com.example.test01.demo.httpModel.auth;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SignUpRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    @NotBlank
    private String countryCode;

    @NotBlank
    private String password;
}
