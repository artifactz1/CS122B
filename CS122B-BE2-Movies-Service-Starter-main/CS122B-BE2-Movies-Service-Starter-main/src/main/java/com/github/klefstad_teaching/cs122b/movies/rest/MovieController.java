package com.github.klefstad_teaching.cs122b.movies.rest;

import java.text.ParseException;
import java.util.List;

import com.github.klefstad_teaching.cs122b.core.error.ResultError;
import com.github.klefstad_teaching.cs122b.core.result.MoviesResults;
import com.github.klefstad_teaching.cs122b.core.result.Result;
import com.github.klefstad_teaching.cs122b.core.security.JWTManager;
import com.github.klefstad_teaching.cs122b.movies.data.Movie;
import com.github.klefstad_teaching.cs122b.movies.repo.MovieRepo;
import com.github.klefstad_teaching.cs122b.movies.request.MovieSearchByPersonID;
import com.github.klefstad_teaching.cs122b.movies.request.MovieSearchRequest;
import com.github.klefstad_teaching.cs122b.movies.response.MovieSearchResponse;
import com.github.klefstad_teaching.cs122b.movies.util.Validate;
import com.nimbusds.jwt.SignedJWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
            MovieSearchRequest rq) throws ParseException {

        List<String> roles = user.getJWTClaimsSet().getStringListClaim(JWTManager.CLAIM_ROLES);

        // Check any errors
        validate.check(rq);

        // Select from data base
        ResponseEntity<MovieSearchResponse> response = repo.selectSearch(rq);
        List<Movie> movies = response.getBody().getMovies();

        // OUTPUT IF NO ERRORS ARE SHOWN
        if (movies.isEmpty()) {
            MovieSearchResponse good = new MovieSearchResponse()
                    .setResult(MoviesResults.NO_MOVIES_FOUND_WITHIN_SEARCH);
            return ResponseEntity.status(HttpStatus.OK).body(good);

        }

        for (int i = 0; i < movies.size(); i++) {
            for (int j = 0; j < roles.size(); j++) {
                if (movies.get(i).getHidden() == true &&
                        roles.get(j).toUpperCase().equals("PREMIUM")) {
                    movies.remove(i);
                }
            }
        }

        MovieSearchResponse good = new MovieSearchResponse()
                .setResult(MoviesResults.MOVIES_FOUND_WITHIN_SEARCH)
                .setMovies(movies);
        return ResponseEntity.status(HttpStatus.OK).body(good);

    }

    @GetMapping("/movie/search/person/{personId}")
    public ResponseEntity<MovieSearchResponse> personID(@AuthenticationPrincipal SignedJWT user,
            MovieSearchByPersonID rq, @PathVariable Integer personId) throws ParseException {

        List<String> roles = user.getJWTClaimsSet().getStringListClaim(JWTManager.CLAIM_ROLES);

        validate.check(rq);

        // Select from data base
        ResponseEntity<MovieSearchResponse> response = repo.selectSearchPersonID(rq, personId);
        List<Movie> movies = response.getBody().getMovies();

        // OUTPUT IF NO ERRORS ARE SHOWN
        if (movies.isEmpty()) {
            MovieSearchResponse good = new MovieSearchResponse()
                    .setResult(MoviesResults.NO_MOVIES_WITH_PERSON_ID_FOUND);
            return ResponseEntity.status(HttpStatus.OK).body(good);

        }

        for (int i = 0; i < movies.size(); i++) {
            for (int j = 0; j < roles.size(); j++) {
                if (movies.get(i).getHidden() == true &&
                        roles.get(j).toUpperCase().equals("PREMIUM")) {
                    movies.remove(i);
                }
            }
        }

        MovieSearchResponse good = new MovieSearchResponse()
                .setResult(MoviesResults.MOVIES_WITH_PERSON_ID_FOUND)
                .setMovies(movies);
        return ResponseEntity.status(HttpStatus.OK).body(good);

    }

}
