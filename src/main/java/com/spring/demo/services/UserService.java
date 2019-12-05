package com.spring.demo.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.spring.demo.db.UserRepository;
import com.spring.demo.entities.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User setGoogleToken(String username, GoogleTokenResponse googleTokenResponse) {
        var user = userRepository.findDistinctFirstByUsernameIgnoreCase(username);
        if(user == null) return null;

        user.setGoogleToken(googleTokenResponse);
        userRepository.save(user);
        return user;
    }
}
