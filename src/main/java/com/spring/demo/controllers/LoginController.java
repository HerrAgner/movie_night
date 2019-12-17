package com.spring.demo.controllers;

import com.spring.demo.entities.User;
import com.spring.demo.services.MyUserDetailsService;
import com.spring.demo.models.AuthenticationRequest;
import com.spring.demo.models.AuthenticationResponse;
import com.spring.demo.services.UserService;
import com.spring.demo.util.FailedLoginHandler;
import com.spring.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("api/login")
public class LoginController {

    @Autowired
    @Resource(name = "authenticationManager")
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtTokenUtil;


    @GetMapping
    public ResponseEntity<HashMap<String, String>> getUsers(Principal principal) {
        HashMap<String, String> userInfo = new HashMap<>();

        User user = userService.getUserByUsername(principal.getName());
        userInfo.put("username", user.getUsername());
        userInfo.put("googleToken", String.valueOf(user.getGoogleToken()));

        return ResponseEntity.ok(userInfo);
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authRequest) throws Exception {
        try {
            if (!FailedLoginHandler.isUserAllowedToLogin(authRequest.getUsername())) {
                var lockoutSeconds = FailedLoginHandler.getLockoutTimeInSeconds(authRequest.getUsername());
                return new ResponseEntity<>(lockoutSeconds, HttpStatus.TOO_MANY_REQUESTS);
            }

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            FailedLoginHandler.addNewFailedRequest(authRequest.getUsername());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        FailedLoginHandler.tryRemoveLoginRequest(authRequest.getUsername());
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
