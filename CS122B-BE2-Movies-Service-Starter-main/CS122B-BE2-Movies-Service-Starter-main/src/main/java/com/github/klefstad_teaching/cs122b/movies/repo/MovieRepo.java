package com.github.klefstad_teaching.cs122b.movies.repo;

import java.sql.Types;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.klefstad_teaching.cs122b.movies.request.MovieSearchByPersonID;
import com.github.klefstad_teaching.cs122b.movies.request.MovieSearchRequest;
import com.github.klefstad_teaching.cs122b.movies.response.MovieSearchResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.github.klefstad_teaching.cs122b.movies.data.Movie;
import com.github.klefstad_teaching.cs122b.movies.data.MovieOrderBy;

@Component
public class MovieRepo {
    private final NamedParameterJdbcTemplate template;

    @Autowired
    public MovieRepo(ObjectMapper objectMapper, NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    private final static String MOVIE_QUERY =

            "SELECT DISTINCT m.id, m.title, m.year, p.name, m.rating, m.backdrop_path, m.poster_path, m.hidden "
                    +
                    "FROM movies.movie as m " +
                    "JOIN movies.person as p on m.director_id = p.id " +
                    "JOIN movies.movie_genre as mG on mG.movie_id = m.id " +
                    "JOIN movies.genre as g on g.id = mG.genre_id ";

    private final static String PERSON_ID_QUERY =

            "SELECT DISTINCT m.id, m.title, m.year, p.name, m.rating, m.backdrop_path, m.poster_path, m.hidden "
                    +
                    "FROM movies.movie as m " +
                    "JOIN movies.movie_person as mp on mp.person_id " +
                    "JOIN movies.person as p on p.id = m.director_id AND m.id = mp.movie_id ";

    @GetMapping("/movie/search")

    public ResponseEntity<MovieSearchResponse> selectSearch(@RequestBody MovieSearchRequest rq) {

        StringBuilder sql;
        MapSqlParameterSource source = new MapSqlParameterSource();
        boolean isWhereAlreadyAdded = false;

        sql = new StringBuilder(MOVIE_QUERY);

        if (rq.getTitle() != null) {

            sql.append(" WHERE m.title LIKE :title");

            String wildcardSearch = '%' + rq.getTitle() + '%';

            source.addValue("title", wildcardSearch, Types.VARCHAR);
            isWhereAlreadyAdded = true;

        }
        if (rq.getYear() != null) {
            if (isWhereAlreadyAdded == true) {
                sql.append(" AND m.year = :year");
            } else {
                isWhereAlreadyAdded = true;
                sql.append(" WHERE m.year = :year");
            }

            source.addValue("year", rq.getYear(), Types.INTEGER);
        }

        if (rq.getDirector() != null) {

            if (isWhereAlreadyAdded == true) {
                sql.append(" AND p.name LIKE :name");
            } else {
                isWhereAlreadyAdded = true;
                sql.append(" Where p.name LIKE :name");
            }
            String wildcardSearch = '%' + rq.getDirector() + '%';
            source.addValue("name", wildcardSearch, Types.VARCHAR);
        }

        if (rq.getGenre() != null) {

            if (isWhereAlreadyAdded == true) {
                sql.append(" AND g.name LIKE :genre");
            } else {
                isWhereAlreadyAdded = true;
                sql.append(" Where g.name LIKE :genre");
            }

            String wildcardSearch = '%' + rq.getGenre() + '%';
            source.addValue("genre", wildcardSearch, Types.VARCHAR);
        }

        MovieOrderBy orderBy = MovieOrderBy.fromString(rq.getOrderBy());
        sql.append(orderBy.toSql());
        sql.append(rq.getDirection().toString());
        sql.append(", m.id ASC");

        // if (rq.getOrderBy() != null) {
        // sql.append(" ORDER BY :orderBy");
        // String add = "m." + rq.getOrderBy();
        // source.addValue("orderBy", add, Types.VARCHAR);
        // }

        // if (rq.getDirection() != null) {
        // sql.append(" :direction");
        // source.addValue("direction", rq.getDirection(), Types.VARCHAR);
        // }

        if (rq.getLimit() != null) {

            sql.append(" LIMIT :limit");
            source.addValue("limit", rq.getLimit(), Types.INTEGER);
        }

        if (rq.getPage() != null) {

            sql.append(" OFFSET :page");
            source.addValue("page", (rq.getPage() - 1) * rq.getLimit(), Types.INTEGER);
        }

        System.out.println(sql);

        List<Movie> movies = this.template.query(
                sql.toString(),
                source,
                (rs, rowNum) -> new Movie()
                        .setId(rs.getInt("m.id"))
                        .setTitle(rs.getString("m.title"))
                        .setYear(rs.getInt("m.year"))
                        .setDirector(rs.getString("p.name"))
                        .setRating(rs.getDouble("m.rating"))
                        .setBackdropPath(rs.getString("m.backdrop_path"))
                        .setPosterPath(rs.getString("m.poster_path"))
                        .setHidden(rs.getBoolean("m.hidden")));

        MovieSearchResponse send = new MovieSearchResponse().setMovies(movies);
        return ResponseEntity.status(HttpStatus.OK).body(send);
    }

    @GetMapping("/movie/search/person/{personId}")
    public ResponseEntity<MovieSearchResponse> selectSearchPersonID(@RequestBody MovieSearchByPersonID rq,
            Integer personId) {

        StringBuilder sql;
        MapSqlParameterSource source = new MapSqlParameterSource();

        sql = new StringBuilder(PERSON_ID_QUERY);

        sql.append(" Where mp.person_id = :personId");
        source.addValue("personId", personId, Types.INTEGER);

        MovieOrderBy orderBy = MovieOrderBy.fromString(rq.getOrderBy());
        sql.append(orderBy.toSql());
        sql.append(rq.getDirection().toString());
        sql.append(", m.id ASC");

        if (rq.getLimit() != null) {

            sql.append(" LIMIT :limit");
            source.addValue("limit", rq.getLimit(), Types.INTEGER);
        }

        if (rq.getPage() != null) {

            sql.append(" OFFSET :page");
            source.addValue("page", (rq.getPage() - 1) * rq.getLimit(), Types.INTEGER);
        }

        System.out.println(sql);

        List<Movie> movies = this.template.query(
                sql.toString(),
                source,
                (rs, rowNum) -> new Movie()
                        .setId(rs.getInt("m.id"))
                        .setTitle(rs.getString("m.title"))
                        .setYear(rs.getInt("m.year"))
                        .setDirector(rs.getString("p.name"))
                        .setRating(rs.getDouble("m.rating"))
                        .setBackdropPath(rs.getString("m.backdrop_path"))
                        .setPosterPath(rs.getString("m.poster_path"))
                        .setHidden(rs.getBoolean("m.hidden")));

        MovieSearchResponse send = new MovieSearchResponse().setMovies(movies);
        return ResponseEntity.status(HttpStatus.OK).body(send);

    }
}
