package com.spring.demo.services;


import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.auth.oauth2.AccessToken;
import com.spring.demo.entities.User;
import com.spring.demo.util.SuperSecretInformation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

@Service
public class GAuthService {

    private final SuperSecretInformation superSecretInformation;
    private final UserService userService;
    private final int REFRESH_LIMIT = 300;

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

    private boolean timeToRefreshToken(long expiresAt) {
        var epoch = Instant.now().getEpochSecond();
        if ((expiresAt - epoch) > REFRESH_LIMIT) {
            return false;
        }
        return true;
    }

    private GoogleTokenResponse refreshToken(User user) {
        var refreshedToken = getRefreshedCredentials(user.getGoogleToken().getRefreshToken());

        if (refreshedToken != null) {
            user.refreshGoogleAccessToken(refreshedToken);
            long expiresAt = Instant.now().getEpochSecond() + 3600;
            user.setGoogleTokenExpiresAt(expiresAt);
            user = userService.saveUser(user);
        }

        return user != null ? user.getGoogleToken() : null;
    }

    public GoogleTokenResponse tryRefreshToken(String username) {
        if (username == null) return null;

        var user = userService.getUserByUsername(username);
        if (user == null) return null;

        var isTimeToRefresh = timeToRefreshToken(user.getGoogleTokenExpiresAt());
        if (isTimeToRefresh) {
            return refreshToken(user);
        }
        return null;
    }

    private GoogleTokenResponse getRefreshedCredentials(String refreshCode) {
        try {
            GoogleTokenResponse response = new GoogleRefreshTokenRequest(
                    new NetHttpTransport(), JacksonFactory.getDefaultInstance(), refreshCode, superSecretInformation.getClientId(), superSecretInformation.getClientSecret())
                    .execute();

            return new GoogleTokenResponse().setAccessToken(response.getAccessToken());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String getBearerTokenForUser(String username) {
        var user = userService.getUserByUsername(username);
        if(user == null) return null;
        if(user.getGoogleToken() == null) return null;

        return user.getGoogleToken().getAccessToken();
    }

    public AccessToken getAccessToken(String username) {
        var user = userService.getUserByUsername(username);
        if(user == null || user.getGoogleToken() == null || user.getGoogleTokenExpiresAt() == 0) return null;
        var accessToken = new AccessToken(user.getGoogleToken().getAccessToken(), new Date(user.getGoogleTokenExpiresAt()));
        return accessToken;
    }


}
