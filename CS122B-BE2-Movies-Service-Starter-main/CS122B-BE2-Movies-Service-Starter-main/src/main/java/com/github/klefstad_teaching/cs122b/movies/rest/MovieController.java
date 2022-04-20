package com.github.klefstad_teaching.cs122b.movies.rest;

import java.text.ParseException;
import java.util.List;

import com.github.klefstad_teaching.cs122b.core.result.MoviesResults;
import com.github.klefstad_teaching.cs122b.movies.data.Movie;
import com.github.klefstad_teaching.cs122b.movies.repo.MovieRepo;
import com.github.klefstad_teaching.cs122b.movies.request.MovieSearchRequest;
import com.github.klefstad_teaching.cs122b.movies.response.MovieSearchResponse;
import com.github.klefstad_teaching.cs122b.movies.util.Validate;
import com.nimbusds.jwt.SignedJWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieController {
    private final MovieRepo repo;
    private final Validate validate;

    @Autowired
    public MovieController(MovieRepo repo, Validate validate) {
        this.repo = repo;
        this.validate = validate;
    }

    @GetMapping("/movie/search")
    public ResponseEntity<MovieSearchResponse> moviesearch(@AuthenticationPrincipal SignedJWT user,
            @RequestBody MovieSearchRequest rq) {

        System.out.println("GET LIMIT" + rq.getLimit());
        System.out.println("GET LIMIT" + rq.getLimit());
        System.out.println("GET LIMIT" + rq.getLimit());
        System.out.println("GET LIMIT" + rq.getLimit());
        System.out.println("GET LIMIT" + rq.getLimit());

        // Check any errors
        validate.check(rq);

        // Select from data base
        ResponseEntity<MovieSearchResponse> response = repo.selectSearch(rq);
        List<Movie> movies = response.getBody().getMovies();

        if (movies.isEmpty()) {
            MovieSearchResponse good = new MovieSearchResponse()
                    .setResult(MoviesResults.NO_MOVIES_FOUND_WITHIN_SEARCH)
                    .setMovies(movies);
            return ResponseEntity.status(HttpStatus.OK).body(good);

        }

        MovieSearchResponse good = new MovieSearchResponse()
                .setResult(MoviesResults.MOVIES_FOUND_WITHIN_SEARCH)
                .setMovies(movies);
        return ResponseEntity.status(HttpStatus.OK).body(good);

    }
}
