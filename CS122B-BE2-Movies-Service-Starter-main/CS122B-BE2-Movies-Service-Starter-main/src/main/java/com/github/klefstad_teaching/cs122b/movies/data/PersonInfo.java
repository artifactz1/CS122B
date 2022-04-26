package com.github.klefstad_teaching.cs122b.movies.data;

import com.fasterxml.jackson.annotation.JsonInclude;

public class PersonInfo {

    private Long id;
    private Float popularity;
    private String name, birthday, biography, birthplace, profilePath;

    public String getBirthday() {
        return birthday;
    }

    public PersonInfo setBirthday(String birthday) {
        this.birthday = birthday;
        return this;
    }

    public String getBiography() {
        return biography;
    }

    public PersonInfo setBiography(String biography) {
        this.biography = biography;
        return this;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public PersonInfo setBirthplace(String birthplace) {
        this.birthplace = birthplace;
        return this;
    }

    public Float getPopularity() {
        return popularity;
    }

    public PersonInfo setPopularity(Float popularity) {
        this.popularity = popularity;
        return this;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public PersonInfo setProfilePath(String profilePath) {
        this.profilePath = profilePath;
        return this;
    }

    public Long getId() {
        return id;
    }

    public PersonInfo setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public PersonInfo setName(String name) {
        this.name = name;
        return this;
    }

}
