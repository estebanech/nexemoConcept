package com.example.test01.demo.service;

import com.authy.AuthyException;
import com.authy.api.Hash;
import com.authy.api.Token;
import com.authy.api.Tokens;
import com.authy.api.User;
import com.authy.api.Users;
import com.example.test01.demo.entity.UserIn;
import com.example.test01.demo.httpModel.auth.LogInRequest;
import com.example.test01.demo.httpModel.auth.SignUpRequest;
import com.example.test01.demo.httpModel.auth.VerifyRequest;
import com.example.test01.demo.repository.UserRepository;
import com.example.test01.demo.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.authy.AuthyApiClient;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthyApiClient authyApiClient;
    private final JwtProvider jwtProvider;
    @Override
    public Optional<UserIn> createUser(final SignUpRequest request){
        final int authyId = createAuthy(request);
        if(!userRepository.existsByAuthyId(authyId)) {
            final UserIn user = UserIn.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .phone(request.getPhone())
                    .countryCode(request.getCountryCode())
                    .authyId(authyId)
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
        sendSMS(user.get().getAuthyId());
        return user;
    }

    @Override
    public Optional<UserIn> verify(VerifyRequest request) {
        try {
            final Tokens tokens = authyApiClient.getTokens();
            final Token response = tokens.verify(request.getAuthyId(), request.getCode());
            if (response.isOk()) {
                return userRepository.findByAuthyId(request.getAuthyId());
            }
            else{
                return Optional.empty();
            }
        } catch (AuthyException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid code for user");
        }
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


    private void sendSMS(final int authyId){
        final Users users = authyApiClient.getUsers();
        try {
            final Hash response = users.requestSms(authyId);
            if (!response.isOk()) {
                throw new RuntimeException("Unable to send SMS");
            }
        } catch (AuthyException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to send SMS");
        }
    }

    private int createAuthy(final SignUpRequest request){
        final Users users = authyApiClient.getUsers();
        final User authyUser;
        try {
            authyUser = users.createUser(
                    request.getEmail(),
                    request.getPhone(),
                    request.getCountryCode());
        } catch (AuthyException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to create user");
        }
        return authyUser.getId();
    }
}
