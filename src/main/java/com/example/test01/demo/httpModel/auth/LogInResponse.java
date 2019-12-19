package com.example.test01.demo.httpModel.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogInResponse {
    private Integer authyId;
}
