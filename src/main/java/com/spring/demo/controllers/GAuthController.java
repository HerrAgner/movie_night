package com.spring.demo.controllers;


import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.spring.demo.services.UserService;
import com.spring.demo.util.SuperSecretInformation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;


@RestController
@RequestMapping("api/gauth")
public class GAuthController {

    private final SuperSecretInformation superSecretInformation;
    private final UserService userService;

    public GAuthController(SuperSecretInformation superSecretInformation, UserService userService) {
        this.superSecretInformation = superSecretInformation;
        this.userService = userService;
    }

    
    @PostMapping
    public ResponseEntity<String> authCode(@RequestBody HashMap<String, String> body, @RequestHeader("X-Requested-With") String encoding) {
        String username = null;
        String code = null;

        try {
            username = body.get("username");
            code = body.get("code");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (username == null || code == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);


        if (encoding == null || encoding.isEmpty()) {
            // Without the `X-Requested-With` header, this request could be forged. Aborts.
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }

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
        Long expiresAt = System.currentTimeMillis() + (tokenResponse.getExpiresInSeconds() * 1000);

        if(tokenResponse != null) {
            userService.setGoogleToken(username, tokenResponse);
        }

        // Debug purpose only
        System.out.println("accessToken: " + accessToken);
        System.out.println("refreshToken: " + refreshToken);
        System.out.println("expiresAt: " + expiresAt);

        return new ResponseEntity<>(HttpStatus.OK);

    }
}
