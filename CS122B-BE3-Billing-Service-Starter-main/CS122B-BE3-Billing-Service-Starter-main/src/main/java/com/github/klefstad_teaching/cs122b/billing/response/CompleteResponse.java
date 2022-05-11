package com.github.klefstad_teaching.cs122b.billing.response;

import com.github.klefstad_teaching.cs122b.core.result.Result;

public class CompleteResponse {
    public Result result;

    public Result getResult() {
        return result;
    }

    public CompleteResponse setResult(Result result) {
        this.result = result;
        return this;
    }

}
