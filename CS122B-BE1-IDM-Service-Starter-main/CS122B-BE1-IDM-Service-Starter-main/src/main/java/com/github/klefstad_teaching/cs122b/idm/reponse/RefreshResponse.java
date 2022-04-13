package com.github.klefstad_teaching.cs122b.idm.reponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.klefstad_teaching.cs122b.core.result.Result;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.RefreshToken;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.User;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RefreshResponse {

    private Result result;
    private String accessToken;
    private String refreshToken;
    private User user;

    public User getUser() {
        return user;
    }

    public RefreshResponse setUser(User user) {
        this.user = user;
        return this;
    }

    public Result getResult() {
        return result;
    }

    public RefreshResponse setResult(Result result) {
        this.result = result;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public RefreshResponse setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public RefreshResponse setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

}
