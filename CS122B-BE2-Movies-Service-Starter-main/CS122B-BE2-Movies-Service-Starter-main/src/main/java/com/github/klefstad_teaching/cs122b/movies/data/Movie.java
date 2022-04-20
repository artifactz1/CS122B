package com.github.klefstad_teaching.cs122b.movies.data;

public class Movie {

    private Integer id, year, director_id, num_votes, budget, revenue;
    private String title, overveiew, backdrop_path, poster_path, director;
    private Double rating;
    private Boolean hidden;

    public Integer getId() {
        return id;
    }

    public Movie setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getYear() {
        return year;
    }

    public Movie setYear(Integer year) {
        this.year = year;
        return this;
    }

    public Integer getDirector_id() {
        return director_id;
    }

    public Movie setDirector_id(Integer director_id) {
        this.director_id = director_id;
        return this;
    }

    public Integer getNum_votes() {
        return num_votes;
    }

    public Movie setNum_votes(Integer num_votes) {
        this.num_votes = num_votes;
        return this;
    }

    public Integer getBudget() {
        return budget;
    }

    public Movie setBudget(Integer budget) {
        this.budget = budget;
        return this;
    }

    public Integer getRevenue() {
        return revenue;
    }

    public Movie setRevenue(Integer revenue) {
        this.revenue = revenue;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Movie setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getOverveiew() {
        return overveiew;
    }

    public Movie setOverveiew(String overveiew) {
        this.overveiew = overveiew;
        return this;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public Movie setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
        return this;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public Movie setPoster_path(String poster_path) {
        this.poster_path = poster_path;
        return this;
    }

    public Double getRating() {
        return rating;
    }

    public Movie setRating(Double rating) {
        this.rating = rating;
        return this;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public Movie setHidden(Boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    public String getDirector() {
        return director;
    }

    public Movie setDirector(String director) {
        this.director = director;
        return this;
    }

}
