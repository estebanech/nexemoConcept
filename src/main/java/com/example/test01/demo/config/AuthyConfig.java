package com.example.test01.demo.config;

import com.authy.AuthyApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthyConfig {

    @Value("${app.authyKey}")
    private String API_KEY;
    @Bean
    public AuthyApiClient creatAuthyClient(){
        return new AuthyApiClient(API_KEY);
    }
}
