package com.github.klefstad_teaching.cs122b.gateway.models;

public class Posts {

    public String accessToken;
    public String body;

    public String getBody() {
        return body;
    }

    public Posts setBody(String body) {
        this.body = body;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Posts setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    @Override
    public String toString() {
        return "Posts{" +
                "body='" + body + '\'' +
                ", accessToken='" + accessToken + '\'' + '}';
    }

}