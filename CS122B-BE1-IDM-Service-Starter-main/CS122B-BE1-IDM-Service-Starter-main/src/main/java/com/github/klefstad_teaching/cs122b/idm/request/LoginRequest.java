package com.github.klefstad_teaching.cs122b.idm.request;

import com.github.klefstad_teaching.cs122b.idm.repo.entity.RefreshToken;

public class LoginRequest {

    private String email;
    private char[] password;

    private RefreshToken refreshToken;

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    public LoginRequest setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public char[] getPassword() {
        return password;
    }

    public LoginRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public LoginRequest setPassword(char[] password) {
        this.password = password;
        return this;
    }
}
