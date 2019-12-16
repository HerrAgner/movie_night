package com.spring.demo.models;

import java.time.Duration;
import java.time.Instant;

public class FailedLoginRequest {

    private final String username;
    private Instant loginAttemptTimestamp;
    private int failedAttempts;
    private final int LOCKOUT_MIN = 1;

    public FailedLoginRequest(String username) {
        this.username = username;
        this.loginAttemptTimestamp = Instant.now();
        this.failedAttempts = 1;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    private Instant getAllowedLoginTime() {
        if (this.getFailedAttempts() >= 3) {
            return this.loginAttemptTimestamp.plus(Duration.ofMinutes(LOCKOUT_MIN));
        } else {
            return Instant.EPOCH;
        }
    }

    private Instant getLoginAttemptTimestamp() {
        return this.loginAttemptTimestamp;
    }

    private void setLoginAttemptTimestampToNow() {
        this.loginAttemptTimestamp = Instant.now();
    }

    private void addOneToFailedAttempts() {
        this.failedAttempts++;
    }

    public void registerNewFailedLoginAttempt() {
        this.setLoginAttemptTimestampToNow();
        this.addOneToFailedAttempts();
    }

    public void resetFailedAttempts() {
        this.failedAttempts = 0;
    }

    public boolean isAllowedLogin() {
        if (this.getAllowedLoginTime().isBefore(Instant.now())) {
            if(this.getFailedAttempts() >= 3) {
                resetFailedAttempts();
            }
            return true;
        }
        return false;
    }

    public long getLockoutTimeInSeconds() {
        return Math.max(this.getAllowedLoginTime().getEpochSecond() - Instant.now().getEpochSecond(), 0);
    }
}
