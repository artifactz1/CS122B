package com.github.klefstad_teaching.cs122b.movies.data;

public class Person {

    private Long personID;
    private String name;

    public Long getPersonID() {
        return personID;
    }

    public Person setPersonID(Long personID) {
        this.personID = personID;
        return this;
    }

    public String getName() {
        return name;
    }

    public Person setName(String name) {
        this.name = name;
        return this;
    }
}
