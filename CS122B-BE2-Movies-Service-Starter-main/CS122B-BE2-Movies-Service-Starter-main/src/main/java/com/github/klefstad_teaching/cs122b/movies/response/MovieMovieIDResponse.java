package com.github.klefstad_teaching.cs122b.movies.response;

import java.sql.Array;
import java.util.List;

import com.github.klefstad_teaching.cs122b.core.result.Result;
import com.github.klefstad_teaching.cs122b.movies.data.Genre;
import com.github.klefstad_teaching.cs122b.movies.data.MovieInfo;
import com.github.klefstad_teaching.cs122b.movies.data.Person;

public class MovieMovieIDResponse {

    private Result result;
    private MovieInfo movies;
    private List<Genre> genres;
    private List<Person> persons;

    public Result getResult() {
        return result;
    }

    public MovieMovieIDResponse setResult(Result result) {
        this.result = result;
        return this;
    }

    public MovieInfo getMovieInfo() {
        return movies;
    }

    public MovieMovieIDResponse setMovieInfo(MovieInfo movieInfo) {
        this.movies = movieInfo;
        return this;
    }

    public MovieInfo getMovies() {
        return movies;
    }

    public MovieMovieIDResponse setMovies(MovieInfo movies) {
        this.movies = movies;
        return this;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public MovieMovieIDResponse setGenres(List<Genre> genres) {
        this.genres = genres;
        return this;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public MovieMovieIDResponse setPersons(List<Person> persons) {
        this.persons = persons;
        return this;
    }

}
