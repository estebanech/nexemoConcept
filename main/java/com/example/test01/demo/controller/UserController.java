package com.example.test01.demo.controller;

import com.example.test01.demo.httpModel.CustomSuccessResponse;
import com.example.test01.demo.httpModel.user.GetAllResponse;
import com.example.test01.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<CustomSuccessResponse<GetAllResponse>> getAll(){
        return ResponseEntity.ok(CustomSuccessResponse.success(GetAllResponse.builder()
                        .users(userService.getAll())
                        .build()));
    }
}
