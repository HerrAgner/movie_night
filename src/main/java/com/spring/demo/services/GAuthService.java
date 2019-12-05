package com.spring.demo.services;


import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.spring.demo.util.SuperSecretInformation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;

@Service
public class GAuthService {

    private final SuperSecretInformation superSecretInformation;
    private final UserService userService;

    public GAuthService(SuperSecretInformation superSecretInformation, UserService userService) {
        this.superSecretInformation = superSecretInformation;
        this.userService = userService;
    }


    public GoogleTokenResponse getTokenResponse(String code) {
        GoogleTokenResponse tokenResponse = null;
        try {
            tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance(),
                    "https://www.googleapis.com/oauth2/v4/token",
                    superSecretInformation.getClientId(),
                    superSecretInformation.getClientSecret(),
                    code,
                    "http://localhost:8080") // Make sure you set the correct port
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Store these 3 in your DB
        String accessToken = tokenResponse.getAccessToken();
        String refreshToken = tokenResponse.getRefreshToken();

        Long expiresAt = Instant.now().getEpochSecond() + (tokenResponse.getExpiresInSeconds());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (tokenResponse != null && username != null) {
            userService.setGoogleToken(username, tokenResponse, expiresAt);
        }

        // Debug purpose only
        System.out.println("accessToken: " + accessToken);
        System.out.println("refreshToken: " + refreshToken);
        System.out.println("expiresAt: " + expiresAt);

        return tokenResponse;
    }

    public boolean timeToRefreshToken(long expiresAt) {
        var epoch = Instant.now().getEpochSecond();
        if((expiresAt - epoch) > 300) {
            return false;
        }
        return true;
    }

    public GoogleTokenResponse refreshToken() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(username == null) return null;

        var user = userService.getUserByUsername(username);


        return user.getGoogleToken();

    }


}
