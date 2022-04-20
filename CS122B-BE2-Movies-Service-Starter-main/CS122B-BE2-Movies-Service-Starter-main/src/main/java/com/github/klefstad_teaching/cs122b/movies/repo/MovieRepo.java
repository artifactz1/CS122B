package com.github.klefstad_teaching.cs122b.movies.repo;

import java.sql.Types;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.klefstad_teaching.cs122b.movies.request.MovieSearchRequest;
import com.github.klefstad_teaching.cs122b.movies.response.MovieSearchResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.github.klefstad_teaching.cs122b.movies.data.Movie;

@Component
public class MovieRepo {
    private final NamedParameterJdbcTemplate template;

    @Autowired
    public MovieRepo(ObjectMapper objectMapper, NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @GetMapping("/movie/search")
    public ResponseEntity<MovieSearchResponse> selectSearch(@RequestBody MovieSearchRequest rq) {

        StringBuilder sql;
        MapSqlParameterSource source = new MapSqlParameterSource();
        boolean isWhereAlreadyAdded = false;

        String m = " SELECT JSON_ARRAYAGG(JSON_OBJECT('id', s.id, 'title', s.title, 'year', s.year, 'director' ,person.name, "
                +
                " 'rating', s.rating, 'backdropPath', s.backdrop_path, 'poster_path', s.poster_path, 'hidden', s.hidden)) as jsonArrayString"
                +
                " FROM movie s" +
                " INNER JOIN person on s.director_id = person.id";

        sql = new StringBuilder(m);

        if (rq.getTitle() != null) {

            sql.append(" Where s.title = :title");
            isWhereAlreadyAdded = true;
            source.addValue("title", rq.getTitle(), Types.VARCHAR);
            // might need to add wildcard

        }
        if (rq.getYear() != null) {
            if (isWhereAlreadyAdded == true) {
                sql.append(" AND s.year = :year");
            } else {
                isWhereAlreadyAdded = true;
                sql.append(" WHERE s.year = :year");
            }

            source.addValue("year", rq.getYear(), Types.INTEGER);
        }
        if (rq.getGenre() != null) {

            if (isWhereAlreadyAdded == true) {
                sql.append(" AND s.genre = :genre");
            } else {
                isWhereAlreadyAdded = true;
                sql.append(" Where s.genre = :genre");
            }
            source.addValue("genre", rq.getGenre(), Types.VARCHAR);
        }

        if (rq.getDirector() != null) {

            // have to get Director ID FROM DATA BASE

            if (isWhereAlreadyAdded == true) {
                sql.append(" AND s.director_id = :director_id");
            } else {
                isWhereAlreadyAdded = true;
                sql.append(" Where s.director_id = :director");
            }
            source.addValue("director_id", rq.getDirector(), Types.VARCHAR);
        }

        if (rq.getOrderBy() != null) {
            sql.append(" ORDER BY :orderBy");
            source.addValue("orderBy", "s." + rq.getOrderBy(), Types.VARCHAR);
        }

        if (rq.getDirection() != null) {
            sql.append(" :direction");
            source.addValue("direction", rq.getDirection(), Types.VARCHAR);
        }

        if (rq.getLimit() != null) {

            sql.append(" LIMIT s.limit = :limit");
            source.addValue("limit", rq.getLimit(), Types.INTEGER);
        }

        if (rq.getPage() != null) {

            sql.append(" OFFSET s.page = :page");
            source.addValue("page", rq.getPage(), Types.INTEGER);
        }

        // NEED TO CHECK ORDER BY LOOK AT STUDENTORDERBYY

        List<Movie> movies = this.template.query(
                sql.toString(),
                source,
                (rs, rowNum) -> new Movie()
                        .setId(rs.getInt("id"))
                        .setTitle(rs.getString("title"))
                        .setYear(rs.getInt("year"))
                        .setDirector(rs.getString("director"))
                        .setDirector_id(null)
                        .setRating(rs.getDouble("rating"))
                        .setBackdrop_path(rs.getString("backdropPath"))
                        .setPoster_path(rs.getString("poster_path"))
                        .setHidden(rs.getBoolean("hidden")));

        MovieSearchResponse send = new MovieSearchResponse().setMovies(movies);
        return null;
    }
}
