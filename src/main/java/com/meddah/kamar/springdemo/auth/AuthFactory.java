package com.meddah.kamar.springdemo.auth;

import com.meddah.kamar.springdemo.Model.User;


abstract public class AuthFactory {
    private static User user = null;

    public static User getUser() {
        return AuthFactory.user;
    }

    public static void setUser(User user) {
        AuthFactory.user = user;
    }
}
