package com.spring.demo.controllers;


import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.spring.demo.services.GAuthService;
import com.spring.demo.services.UserService;
import com.spring.demo.util.SuperSecretInformation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;


@RestController
@RequestMapping("api/gauth")
public class GAuthController {

    private final GAuthService gAuthService;

    public GAuthController(GAuthService gAuthService) {
        this.gAuthService = gAuthService;
    }


    @PostMapping
    public ResponseEntity<String> authCode(@RequestBody String code, @RequestHeader("X-Requested-With") String encoding) {

        if (encoding == null || encoding.isEmpty()) {
            // Without the `X-Requested-With` header, this request could be forged. Aborts.
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }

        var tokenResponse = gAuthService.getTokenResponse(code);
        if(tokenResponse == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(HttpStatus.OK);

    }

//    @GetMapping
//    public ResponseEntity<GoogleTokenResponse> refresh(){
//        var refreshed = gAuthService.tryRefreshToken();
//        return new ResponseEntity(refreshed, HttpStatus.OK);
//    }
}
