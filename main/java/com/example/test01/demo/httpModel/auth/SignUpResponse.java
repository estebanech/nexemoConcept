package com.example.test01.demo.httpModel.auth;

import com.example.test01.demo.entity.UserIn;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpResponse {
    private final UserIn user;
}
