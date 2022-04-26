package com.github.klefstad_teaching.cs122b.movies.data;

import java.util.Locale;

import com.github.klefstad_teaching.cs122b.core.error.ResultError;
import com.github.klefstad_teaching.cs122b.core.result.MoviesResults;

public enum PersonOrderBy {

    NAME(" ORDER BY p.name "),
    POPULARITY(" ORDER BY p.popularity "),
    BIRTHDAY(" ORDER BY p.birthday ");

    private final String sql;

    PersonOrderBy(String sql) {
        this.sql = sql;
    }

    public String toSql() {
        return sql;
    }

    public static PersonOrderBy fromString(String orderBy) {
        if (orderBy == null) {
            return NAME;
        }

        switch (orderBy.toUpperCase(Locale.ROOT)) {
            case "NAME":
                return NAME;
            case "POPULARITY":
                return POPULARITY;
            case "BIRTHDAY":
                return BIRTHDAY;
            default:
                throw new ResultError(MoviesResults.INVALID_ORDER_BY);
        }
    }
}
