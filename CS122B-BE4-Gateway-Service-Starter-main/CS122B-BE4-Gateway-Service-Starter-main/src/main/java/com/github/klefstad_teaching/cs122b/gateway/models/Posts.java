package com.github.klefstad_teaching.cs122b.gateway.models;

public class Posts {

    public String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public Posts setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    @Override
    public String toString() {
        return "Todos{" +
                "accessToken=" + accessToken +
                '}';
    }

}