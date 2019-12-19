package com.example.test01.demo.httpModel.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtAuthResponse {
    private String authToken;
    private String refreshToken;
    @Builder.Default
    private String type = "Bearer";
}
