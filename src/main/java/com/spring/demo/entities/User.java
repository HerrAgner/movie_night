package com.spring.demo.entities;

import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document("users")
public class User {
    @Id
    private String id;

    private String username;
    private String password;
    private GoogleTokenResponse googleToken;
    private Set<String> roles;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.googleToken = null;
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
}
