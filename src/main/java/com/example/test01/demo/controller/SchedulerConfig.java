package com.example.test01.demo.controller;

import com.example.test01.demo.repository.VerifyRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
@AllArgsConstructor
public class SchedulerConfig {
    private final VerifyRepository verifyRepository;

    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void purgeExpiredVerifications(){
        verifyRepository.deleteAllByExpirationBefore(LocalDateTime.now());
    }

}
