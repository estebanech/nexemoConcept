package com.example.test01.demo.repository;

import com.example.test01.demo.entity.UserIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserIn,Long> {
    boolean existsByEmail(String email);
    Optional<UserIn> findByEmail(String email);
}
