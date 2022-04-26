package com.github.klefstad_teaching.cs122b.movies.request;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonSearchRequest {

    private String name, birthday, movieTitle, orderBy, direction;
    private Integer limit, page;

    public String getName() {
        return name;
    }

    public PersonSearchRequest setName(String name) {
        this.name = name;
        return this;
    }

    public String getBirthday() {
        return birthday;
    }

    public PersonSearchRequest setBirthday(String birthday) {
        this.birthday = birthday;
        return this;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public PersonSearchRequest setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
        return this;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public PersonSearchRequest setOrderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public String getDirection() {
        return direction;
    }

    public PersonSearchRequest setDirection(String direction) {
        this.direction = direction;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public PersonSearchRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public Integer getPage() {
        return page;
    }

    public PersonSearchRequest setPage(Integer page) {
        this.page = page;
        return this;
    }

}
