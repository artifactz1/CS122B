package com.github.klefstad_teaching.cs122b.movies.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.klefstad_teaching.cs122b.core.result.Result;
import com.github.klefstad_teaching.cs122b.movies.data.Genre;
import com.github.klefstad_teaching.cs122b.movies.data.MovieInfo;
import com.github.klefstad_teaching.cs122b.movies.data.Person;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieMovieIDResponse {

    private Result result;
    private MovieInfo movie;
    private List<Genre> genres;
    private List<Person> persons;

    public Result getResult() {
        return result;
    }

    public MovieMovieIDResponse setResult(Result result) {
        this.result = result;
        return this;
    }

    public MovieInfo getMovie() {
        return movie;
    }

    public MovieMovieIDResponse setMovies(MovieInfo movies) {
        this.movie = movies;
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
