package com.github.klefstad_teaching.cs122b.idm.reponse;

import com.github.klefstad_teaching.cs122b.core.result.Result;

public class RegisterResponse {

    private Result result;

    public Result getResult() {
        return result;
    }

    public RegisterResponse setResult(Result result) {
        this.result = result;
        return this;
    }
}