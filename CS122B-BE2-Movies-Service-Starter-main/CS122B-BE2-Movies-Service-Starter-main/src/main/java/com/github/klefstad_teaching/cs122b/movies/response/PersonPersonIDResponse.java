package com.github.klefstad_teaching.cs122b.movies.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.klefstad_teaching.cs122b.core.result.Result;
import com.github.klefstad_teaching.cs122b.movies.data.PersonInfo;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonPersonIDResponse {

    private Result result;
    private PersonInfo person;

    public Result getResult() {
        return result;
    }

    public PersonPersonIDResponse setResult(Result results) {
        this.result = results;
        return this;
    }

    public PersonInfo getPerson() {
        return person;
    }

    public PersonPersonIDResponse setPerson(PersonInfo person) {
        this.person = person;
        return this;
    }

}
