package com.spring.demo.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.spring.demo.db.UserRepository;
import com.spring.demo.entities.User;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User setGoogleToken(String username, GoogleTokenResponse googleTokenResponse, long expiresAt) {
        var user = userRepository.findDistinctFirstByUsernameIgnoreCase(username);
        if (user == null) return null;

        user.setGoogleToken(googleTokenResponse);
        user.setGoogleTokenExpiresAt(expiresAt);
        try {
            user.setGoogleInfo(googleTokenResponse.parseIdToken().getPayload());
        } catch (IOException e) {
            e.printStackTrace();
        }
        userRepository.save(user);
        return user;
    }

    public User getUserByUsername(String username) {
        return userRepository.findDistinctFirstByUsernameIgnoreCase(username);
    }

    public User findUserByUsername(String username) {
        User user = userRepository.findDistinctFirstByUsernameIgnoreCase("user");
        return user;
    }

    public User saveUser(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            return null;
        }
    }
}
