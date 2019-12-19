package com.example.test01.demo.httpModel.auth;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class VerifyRequest {

    @NotNull
    private int authyId;

    @NotBlank
    private String code;
}
