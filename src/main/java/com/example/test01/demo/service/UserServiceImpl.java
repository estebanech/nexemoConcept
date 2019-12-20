package com.example.test01.demo.service;

import com.example.test01.demo.entity.UserIn;
import com.example.test01.demo.entity.VerifyIn;
import com.example.test01.demo.httpModel.auth.LogInRequest;
import com.example.test01.demo.httpModel.auth.SignUpRequest;
import com.example.test01.demo.httpModel.auth.VerifyInRequest;
import com.example.test01.demo.repository.UserRepository;
import com.example.test01.demo.repository.VerifyRepository;
import com.example.test01.demo.security.JwtProvider;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.verify.VerifyClient;
import com.nexmo.client.verify.VerifyRequest;
import com.nexmo.client.verify.VerifyResponse;
import com.nexmo.client.verify.VerifyStatus;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final VerifyRepository verifyRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerifyClient verifyClient;
    private final JwtProvider jwtProvider;

    private static final int EXPIRATION_INCREMENT = 5;

    @Override
    public Optional<UserIn> createUser(final SignUpRequest request){
        if(!userRepository.existsByEmail(request.getEmail())) {
            final UserIn user = UserIn.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .phone(request.getPhone())
                    .build();
            userRepository.save(user);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserIn> logIn(LogInRequest request) {
        final Optional<UserIn> user = userRepository.findByEmail(request.getEmail());
        if(!user.isPresent() || !passwordEncoder.matches(request.getPassword(),user.get().getPassword())){
            return Optional.empty();
        }
        sendSMS(user.get());
        return user;
    }
    @Override
    public Optional<UserIn> verify(VerifyInRequest request){
        final Optional<VerifyIn> verification = verifyRepository.findByUserId(request.getUserId());
        if(verification.isPresent() && verifyClient.check(verification.get().getNexmoId(),request.getCode()).getStatus().equals(VerifyStatus.OK)){
            verifyRepository.delete(verification.get());
            return userRepository.findById(request.getUserId());
        }
        return Optional.empty();
    }

    @Override
    public List<UserIn> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserIn> refreshToken(String refreshToken) {
        if (jwtProvider.ValidateRefreshToken(refreshToken)) {
            final Long id = jwtProvider.getUserIdFromRefreshJWT(refreshToken);
            return userRepository.findById(id);
        }
        throw new RuntimeException("Invalid token");
    }

    private void sendSMS(final UserIn user){

        try {
            final VerifyRequest request = new VerifyRequest(user.getPhone(), "nexmo-app");
            request.setLength(6);
            final VerifyResponse response = verifyClient.verify(request);
            if (response.getStatus().equals(VerifyStatus.OK)) {
                final String requestId = response.getRequestId();
                final LocalDateTime now = LocalDateTime.now().plusMinutes(EXPIRATION_INCREMENT);
                final VerifyIn verification = verifyRepository.findByUserId(user.getId())
                        .orElse(VerifyIn.builder()
                                .user(user)
                                .build());
                verification.setExpiration(now);
                verification.setNexmoId(requestId);
                verifyRepository.save(verification);
            }
            else{
                throw new RuntimeException("Unable to send the SMS");
            }
        } catch ( NexmoClientException e) {
            throw new RuntimeException(e);
        }
    }
}
