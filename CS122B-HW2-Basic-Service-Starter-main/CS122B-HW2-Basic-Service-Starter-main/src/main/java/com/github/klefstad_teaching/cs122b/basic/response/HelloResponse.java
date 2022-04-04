package com.github.klefstad_teaching.cs122b.basic.response;

import com.github.klefstad_teaching.cs122b.core.result.Result;

public class HelloResponse {
    private Result result;
    private String greeting;

    public Result getResult() {
        return result;
    }

    public HelloResponse setResult(Result result) {
        this.result = result;
        return this;
    }

    public String getGreeting() {
        return greeting;
    }

    public HelloResponse setGreeting(String greeting) {
        this.greeting = greeting;
        return this;
    }
}
