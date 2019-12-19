package com.example.test01.demo.controller;

import com.example.test01.demo.entity.UserIn;
import com.example.test01.demo.httpModel.CustomErrorResponse;
import com.example.test01.demo.httpModel.CustomResponse;
import com.example.test01.demo.httpModel.CustomSuccessResponse;
import com.example.test01.demo.httpModel.auth.JwtAuthResponse;
import com.example.test01.demo.httpModel.auth.LogInRequest;
import com.example.test01.demo.httpModel.auth.LogInResponse;
import com.example.test01.demo.httpModel.auth.SignUpRequest;
import com.example.test01.demo.httpModel.auth.VerifyRequest;
import com.example.test01.demo.security.JwtProvider;
import com.example.test01.demo.service.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final UserServiceImpl userService;
    private final JwtProvider jwtProvider;

    @PostMapping("/signup")
    public ResponseEntity<CustomResponse<JwtAuthResponse>> signUp(final @Validated @RequestBody SignUpRequest request){
        final Optional<UserIn> user = userService.createUser(request);
        if(user.isPresent())
            return ResponseEntity.ok(CustomSuccessResponse.success(generateJWTResponse(user.get())));
        else{
            return ResponseEntity.badRequest().body(CustomErrorResponse.fail("Unable to create User",JwtAuthResponse.class));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<CustomResponse<LogInResponse>> logIn(final @Validated @RequestBody LogInRequest request){
        final Optional<UserIn> user = userService.logIn(request);
        if(user.isPresent()){
            return ResponseEntity.ok(CustomSuccessResponse.success(LogInResponse.builder()
                            .authyId(user.get().getAuthyId())
                            .build()));
        }
        return ResponseEntity.badRequest().body(CustomErrorResponse.fail("Bad User Information", LogInResponse.class));
    }

    @PostMapping("/verify")
    public ResponseEntity<CustomResponse<JwtAuthResponse>> verify(final @Validated @RequestBody VerifyRequest request){
        final Optional<UserIn> user = userService.verify(request);
        if(user.isPresent()){
            return ResponseEntity.ok(CustomSuccessResponse.success(generateJWTResponse(user.get())));
        }
        return ResponseEntity.badRequest().body(CustomErrorResponse.fail("Invalid Code", JwtAuthResponse.class));
    }

    @GetMapping("/refresh")
    public ResponseEntity<CustomResponse<JwtAuthResponse>> refresh(final @Validated @RequestHeader(value = "refreshToken") String token){
        final Optional<UserIn> user = userService.refreshToken(token);
        if(user.isPresent()){
            return ResponseEntity.ok(CustomSuccessResponse.success(generateJWTResponse(user.get())));
        }
        return ResponseEntity.badRequest().body(CustomErrorResponse.fail("Unable to Find a user for the Token", JwtAuthResponse.class));
    }

    private JwtAuthResponse generateJWTResponse(final UserIn user){
        return JwtAuthResponse.builder()
                .authToken(jwtProvider.generateToken(user))
                .refreshToken(jwtProvider.generateRefresh(user))
                .build();
    }
}
