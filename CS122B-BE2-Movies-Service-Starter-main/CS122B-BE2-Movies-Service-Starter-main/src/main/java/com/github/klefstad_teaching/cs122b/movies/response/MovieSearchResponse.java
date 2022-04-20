package com.github.klefstad_teaching.cs122b.movies.response;

import java.util.List;
import com.github.klefstad_teaching.cs122b.core.result.Result;

public class MovieSearchResponse {

    private Result result;
    private List<Movie> movies;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

}
