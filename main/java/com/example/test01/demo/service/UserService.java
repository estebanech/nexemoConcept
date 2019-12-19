package com.example.test01.demo.service;

import com.example.test01.demo.entity.UserIn;
import com.example.test01.demo.httpModel.auth.LogInRequest;
import com.example.test01.demo.httpModel.auth.SignUpRequest;
import com.example.test01.demo.httpModel.auth.VerifyRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserIn> createUser(SignUpRequest request);
    Optional<UserIn> logIn(LogInRequest request);
    Optional<UserIn> verify(VerifyRequest request);
    Optional<UserIn> refreshToken(String refreshToken);
    List<UserIn> getAll();
}
