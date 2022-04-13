
package com.github.klefstad_teaching.cs122b.idm.reponse;

import com.github.klefstad_teaching.cs122b.core.result.Result;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.RefreshToken;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.User;

public class RefreshTKResponse {

    private Result result;
    private String accessToken;
    private RefreshToken refreshToken;
    private User user;

    public User getUser() {
        return user;
    }

    public RefreshTKResponse setUser(User user) {
        this.user = user;
        return this;
    }

    public Result getResult() {
        return result;
    }

    public RefreshTKResponse setResult(Result result) {
        this.result = result;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public RefreshTKResponse setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    public RefreshTKResponse setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

}
