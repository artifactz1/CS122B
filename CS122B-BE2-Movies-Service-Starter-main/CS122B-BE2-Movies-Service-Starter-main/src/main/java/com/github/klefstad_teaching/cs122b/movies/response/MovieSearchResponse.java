package com.github.klefstad_teaching.cs122b.movies.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.klefstad_teaching.cs122b.core.result.Result;
import com.github.klefstad_teaching.cs122b.movies.data.Movie;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieSearchResponse {

    private Result result;
    private List<Movie> movies;

    public Result getResult() {
        return result;
    }

    public MovieSearchResponse setResult(Result result) {
        this.result = result;
        return this;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public MovieSearchResponse setMovies(List<Movie> movies) {
        this.movies = movies;
        return this;
    }

}
