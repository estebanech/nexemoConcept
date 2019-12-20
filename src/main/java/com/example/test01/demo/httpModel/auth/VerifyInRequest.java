package com.example.test01.demo.httpModel.auth;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class VerifyInRequest {

    @NotNull
    private Long userId;

    @NotBlank
    private String code;
}
