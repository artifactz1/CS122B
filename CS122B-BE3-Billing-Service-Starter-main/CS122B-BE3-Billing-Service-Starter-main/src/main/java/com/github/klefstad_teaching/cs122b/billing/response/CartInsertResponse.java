package com.github.klefstad_teaching.cs122b.billing.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.klefstad_teaching.cs122b.core.result.Result;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartInsertResponse {

    private Result result;

    public Result getResult() {
        return result;
    }

    public CartInsertResponse setResult(Result result) {
        this.result = result;
        return this;
    }

}
