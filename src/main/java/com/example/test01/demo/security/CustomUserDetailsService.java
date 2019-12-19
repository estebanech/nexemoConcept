package com.example.test01.demo.security;


import com.example.test01.demo.entity.UserIn;
import com.example.test01.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CustomUserDetailsService {
    private final UserRepository userRepository;

    @Transactional
    public UserDetails loadUserById(Long id) {
        UserIn user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id)
        );

        return CustomUserDetails.create(user);
    }
}
