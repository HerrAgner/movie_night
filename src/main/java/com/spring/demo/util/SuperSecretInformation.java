package com.spring.demo.util;


import org.springframework.stereotype.Service;

@Service
public class SuperSecretInformation {

    private final String CLIENT_ID;
    private final String CLIENT_SECRET;
    private final String APPLICATION_NAME;

    public SuperSecretInformation() {
        CLIENT_ID = "988102945544-klqauldh975vifp1vf5ea6u69qi9ji53.apps.googleusercontent.com";
        CLIENT_SECRET = "t3XsS0XUHNPJWLoZwmB15uuh";
        APPLICATION_NAME = "Movie Nights";
    }

    public String getClientId() {
        return CLIENT_ID;
    }

    public String getClientSecret() {
        return CLIENT_SECRET;
    }

    public String getApplicationName() { return APPLICATION_NAME;}
}