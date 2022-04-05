package com.github.klefstad_teaching.cs122b.basic.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.klefstad_teaching.cs122b.core.result.Result;

// if its null dont display
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReverseResponse {

    private Result result;
    private String reversed;

    public Result getResult() {
        return result;
    }

    public ReverseResponse setResult(Result result) {
        this.result = result;
        return this;
    }

    public String getReversed() {
        return reversed;
    }

    public ReverseResponse setReversed(String reversed) {
        this.reversed = reversed;
        return this;

    }

}
