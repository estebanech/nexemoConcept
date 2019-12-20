package com.example.test01.demo.repository;

import com.example.test01.demo.entity.UserIn;
import com.example.test01.demo.entity.VerifyIn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VerifyRepository extends JpaRepository<VerifyIn,Long> {

    Optional<VerifyIn> findByUserId(Long userId);
    void deleteAllByExpirationBefore(LocalDateTime expiration);
    boolean existsByUser(UserIn user);
}
