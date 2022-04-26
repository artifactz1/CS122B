package com.github.klefstad_teaching.cs122b.movies.data;

public class MovieInfo {
    private Long id, numVotes, budget, revenue;
    private Integer year;
    private String title, director, overview, backdropPath, posterPath;
    private Double rating;
    private Boolean hidden;

    public Long getId() {
        return id;
    }

    public MovieInfo setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getNumVotes() {
        return numVotes;
    }

    public MovieInfo setNumVotes(Long numVotes) {
        this.numVotes = numVotes;
        return this;
    }

    public Long getBudget() {
        return budget;
    }

    public MovieInfo setBudget(Long budget) {
        this.budget = budget;
        return this;
    }

    public Long getRevenue() {
        return revenue;
    }

    public MovieInfo setRevenue(Long revenue) {
        this.revenue = revenue;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public MovieInfo setTitle(String title) {
        this.title = title;
        return this;
    }

    public Integer getYear() {
        return year;
    }

    public MovieInfo setYear(Integer year) {
        this.year = year;
        return this;
    }

    public String getDirector() {
        return director;
    }

    public MovieInfo setDirector(String director) {
        this.director = director;
        return this;
    }

    public String getOverview() {
        return overview;
    }

    public MovieInfo setOverview(String overview) {
        this.overview = overview;
        return this;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public MovieInfo setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
        return this;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public MovieInfo setPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }

    public Double getRating() {
        return rating;
    }

    public MovieInfo setRating(Double rating) {
        this.rating = rating;
        return this;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public MovieInfo setHidden(Boolean hidden) {
        this.hidden = hidden;
        return this;
    }
}
