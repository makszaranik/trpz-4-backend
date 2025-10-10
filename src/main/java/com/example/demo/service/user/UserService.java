package com.example.demo.service.user;

import com.example.demo.dto.auth.RegistrationRequest;
import com.example.demo.exceptions.InvalidCredentialsException;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.model.user.UserEntity;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public UserEntity authenticateAndGetDetails(String username, String rawPassword) {
        UserEntity user = findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Invalid login or username"));

        if (!rawPassword.equals(user.getPassword())) {
            throw new InvalidCredentialsException("Invalid login or username");
        }
        return user;
    }

    public UserEntity registerNewUser(RegistrationRequest registrationRequest) {
        if (userRepository.findByUsername(registrationRequest.username()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }

        UserEntity newUser = UserEntity.builder()
                .username(registrationRequest.username())
                .password(registrationRequest.password())
                .email(registrationRequest.email())
                .role(registrationRequest.role())
                .build();

        return userRepository.save(newUser);
    }

}
