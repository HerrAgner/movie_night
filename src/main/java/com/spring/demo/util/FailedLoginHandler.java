package com.spring.demo.util;

import com.spring.demo.models.FailedLoginRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class FailedLoginHandler {

    private static final Map<String, FailedLoginRequest> failedRequests = new HashMap<>();

    public FailedLoginHandler() {
    }

    public static void addNewFailedRequest(String username) {
        if (failedRequests.containsKey(username)) {
            failedRequests.get(username).registerNewFailedLoginAttempt();
        } else {
            failedRequests.put(username, new FailedLoginRequest(username));
        }
    }

    public static boolean isUserAllowedToLogin(String username) {
        return !failedRequests.containsKey(username) || failedRequests.get(username).isAllowedLogin();
    }

    public static long getLockoutTimeInSeconds(String username) {
        return failedRequests.containsKey(username) ? failedRequests.get(username).getLockoutTimeInSeconds() : 0;
    }

    public static void tryRemoveLoginRequest(String username) {
        if (failedRequests.containsKey(username)) {
            failedRequests.remove(username);
        }
    }

}
