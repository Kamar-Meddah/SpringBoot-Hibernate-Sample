package com.meddah.kamar.springdemo.Security.auth;

import com.meddah.kamar.springdemo.Model.User;


abstract public class AuthFactory {
    private static User AuthenticatedUser = null;

    public static User getUser() {
        return AuthFactory.AuthenticatedUser;
    }

    public static void setUser(User user) {
        AuthFactory.AuthenticatedUser = user;
    }
}
