package com.github.klefstad_teaching.cs122b.movies.request;

public class MovieSearchByPersonID {

    Integer limit, page;
    String orderBy, direction;

    public Integer getLimit() {
        return limit;
    }

    public MovieSearchByPersonID setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public Integer getPage() {
        return page;
    }

    public MovieSearchByPersonID setPage(Integer page) {
        this.page = page;
        return this;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public MovieSearchByPersonID setOrderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public String getDirection() {
        return direction;
    }

    public MovieSearchByPersonID setDirection(String direction) {
        this.direction = direction;
        return this;
    }

}
