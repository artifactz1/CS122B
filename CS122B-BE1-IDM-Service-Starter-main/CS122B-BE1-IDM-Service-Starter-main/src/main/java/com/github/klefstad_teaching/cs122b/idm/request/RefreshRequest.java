package com.github.klefstad_teaching.cs122b.idm.request;

import com.github.klefstad_teaching.cs122b.idm.repo.entity.RefreshToken;

public class RefreshRequest {
    private RefreshToken refreshToken;

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    public RefreshRequest setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

}
