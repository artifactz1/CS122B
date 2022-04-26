package com.github.klefstad_teaching.cs122b.movies.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.klefstad_teaching.cs122b.core.result.Result;
import com.github.klefstad_teaching.cs122b.movies.data.Person;
import com.github.klefstad_teaching.cs122b.movies.data.PersonInfo;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonSearchResponse {

    private Result result;
    private List<PersonInfo> persons;

    public Result getResult() {
        return result;
    }

    public PersonSearchResponse setResult(Result result) {
        this.result = result;
        return this;
    }

    public List<PersonInfo> getPersons() {
        return persons;
    }

    public PersonSearchResponse setPersons(List<PersonInfo> persons) {
        this.persons = persons;
        return this;
    }

}
