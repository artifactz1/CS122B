package com.github.klefstad_teaching.cs122b.movies.rest;

import java.text.ParseException;
import java.util.List;

import com.github.klefstad_teaching.cs122b.core.result.MoviesResults;
import com.github.klefstad_teaching.cs122b.core.result.Result;
import com.github.klefstad_teaching.cs122b.core.security.JWTManager;
import com.github.klefstad_teaching.cs122b.movies.data.Person;
import com.github.klefstad_teaching.cs122b.movies.data.PersonInfo;
import com.github.klefstad_teaching.cs122b.movies.repo.MovieRepo;
import com.github.klefstad_teaching.cs122b.movies.request.PersonSearchRequest;
import com.github.klefstad_teaching.cs122b.movies.response.PersonPersonIDResponse;
import com.github.klefstad_teaching.cs122b.movies.response.PersonSearchResponse;
import com.github.klefstad_teaching.cs122b.movies.util.Validate;
import com.nimbusds.jwt.SignedJWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {
    private final MovieRepo repo;
    private final Validate validate;

    @Autowired
    public PersonController(MovieRepo repo, Validate validate) {
        this.repo = repo;
        this.validate = validate;
    }

    @GetMapping("/person/search")
    public ResponseEntity<PersonSearchResponse> moviesearch(@AuthenticationPrincipal SignedJWT user,
            PersonSearchRequest rq) throws ParseException {

        // List<String> roles =
        // user.getJWTClaimsSet().getStringListClaim(JWTManager.CLAIM_ROLES);
        validate.check(rq);

        ResponseEntity<PersonSearchResponse> response = repo.personSearch(rq);
        List<PersonInfo> persons = response.getBody().getPersons();

        if (persons.isEmpty()) {
            PersonSearchResponse good = new PersonSearchResponse()
                    .setResult(MoviesResults.NO_PERSONS_FOUND_WITHIN_SEARCH);
            return ResponseEntity.status(HttpStatus.OK).body(good);
        }

        PersonSearchResponse good = new PersonSearchResponse()
                .setResult(MoviesResults.PERSONS_FOUND_WITHIN_SEARCH)
                .setPersons(persons);
        return ResponseEntity.status(HttpStatus.OK).body(good);

    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<PersonPersonIDResponse> movieID(@AuthenticationPrincipal SignedJWT user,
            @PathVariable Long personId) throws ParseException {

        ResponseEntity<PersonPersonIDResponse> response = repo.personSearchPersonID(personId);
        Result result = response.getBody().getResult();
        PersonInfo person = response.getBody().getPerson();

        PersonPersonIDResponse send = new PersonPersonIDResponse()
                .setResult(result)
                .setPerson(person);

        return ResponseEntity.status(HttpStatus.OK).body(send);
    }
}
