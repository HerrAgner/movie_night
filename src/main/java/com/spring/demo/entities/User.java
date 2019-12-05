package com.spring.demo.entities;

import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.SQLOutput;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Document("users")
public class User {
    @Id
    private String id;

    private String username;
    private String password;
    private GoogleTokenResponse googleToken;
    private long googleTokenExpiresAt;
    private Set<String> roles;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.googleToken = null;
        this.googleTokenExpiresAt = 0;
        this.roles = new HashSet<>();
        this.roles.add("USER");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addRole(String role) {
        this.roles.add(role);
    }

    public void removeRole(String role) {
        this.roles.remove(role);
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public GoogleTokenResponse getGoogleToken() {
        return googleToken;
    }

    public void setGoogleToken(GoogleTokenResponse googleToken) {
        this.googleToken = googleToken;
    }

    public long getGoogleTokenExpiresAt() {
        return googleTokenExpiresAt;
    }

    public void setGoogleTokenExpiresAt(long googleTokenExpiresAt) {
        this.googleTokenExpiresAt = googleTokenExpiresAt;
    }

    public void refreshGoogleAccessToken(GoogleTokenResponse googleTokenResponse) {
        try {
            this.googleToken.setAccessToken(googleTokenResponse.getAccessToken());
        } catch (Exception e) {
            System.out.println("GoogleAccessToken not set!");
        }
    }
}
