package com.example.test01.demo.httpModel.auth;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VerifyResponse {
    private String token;
    @Builder.Default
    private String type = "Bearer";
}
