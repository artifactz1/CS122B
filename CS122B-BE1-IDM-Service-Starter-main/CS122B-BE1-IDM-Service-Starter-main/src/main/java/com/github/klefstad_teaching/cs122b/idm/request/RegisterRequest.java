package com.github.klefstad_teaching.cs122b.idm.request;

import com.github.klefstad_teaching.cs122b.idm.repo.entity.User;

public class RegisterRequest {

    private String email;
    private char[] password;
    private User user;

    public User getUser() {
        return user;
    }

    public String getEmail() {
        return email;
    }

    public char[] getPassword() {
        return password;
    }

    public RegisterRequest setUser(User user) {
        this.user = user;
        return this;
    }

    public RegisterRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public RegisterRequest setPassword(char[] password) {
        this.password = password;
        return this;
    }
}
