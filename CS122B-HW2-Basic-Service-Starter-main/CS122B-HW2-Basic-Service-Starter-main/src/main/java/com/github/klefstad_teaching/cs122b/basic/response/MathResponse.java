package com.github.klefstad_teaching.cs122b.basic.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.klefstad_teaching.cs122b.core.result.Result;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MathResponse {
    private Integer value;
    private Result result;

    public Integer getValue() {
        return value;
    }

    public MathResponse setValue(Integer value) {
        this.value = value;
        return this;
    }

    public Result getResult() {
        return result;
    }

    public MathResponse setResult(Result result) {
        this.result = result;
        return this;
    }

}
