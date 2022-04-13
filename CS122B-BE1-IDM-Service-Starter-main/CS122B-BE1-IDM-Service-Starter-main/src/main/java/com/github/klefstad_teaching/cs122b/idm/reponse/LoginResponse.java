package com.github.klefstad_teaching.cs122b.idm.reponse;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.klefstad_teaching.cs122b.core.result.Result;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.RefreshToken;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.User;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {

    private Result result;
    private String accessToken;
    private String refreshToken;
    private List<User> users;

    public Result getResult() {
        return result;
    }

    public LoginResponse setResult(Result result) {
        this.result = result;
        return this;
    }

    public List<User> getUsers() {
        return users;
    }

    public LoginResponse setUsers(List<User> users) {
        this.users = users;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public LoginResponse setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public LoginResponse setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

}
