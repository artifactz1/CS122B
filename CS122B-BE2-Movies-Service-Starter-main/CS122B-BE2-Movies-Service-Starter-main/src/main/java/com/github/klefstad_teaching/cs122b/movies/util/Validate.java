package com.github.klefstad_teaching.cs122b.movies.util;

import com.github.klefstad_teaching.cs122b.core.error.ResultError;
import com.github.klefstad_teaching.cs122b.core.result.MoviesResults;
import com.github.klefstad_teaching.cs122b.movies.request.MovieSearchRequest;

import org.springframework.stereotype.Component;

@Component
public class Validate {

    public void check(MovieSearchRequest rq) {
        if (rq.getOrderBy().equals("title") == false
                && rq.getOrderBy().equals("rating") == false
                && rq.equals("year") == false) {
            throw new ResultError(MoviesResults.INVALID_ORDER_BY);
        }

        if (rq.getDirection().equals("asc") == false && rq.getDirection().equals("desc") == false) {
            throw new ResultError(MoviesResults.INVALID_ORDER_BY);
        }

        if (rq.getLimit() != 10 && rq.getLimit() != 25 && rq.getLimit() != 50 && rq.getLimit() != 100) {
            throw new ResultError(MoviesResults.INVALID_LIMIT);
        }
        if (rq.getPage() <= 0) {
            throw new ResultError(MoviesResults.INVALID_PAGE);
        }
    }
}
