package com.github.klefstad_teaching.cs122b.movies.repo;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.klefstad_teaching.cs122b.movies.request.MovieSearchByPersonID;
import com.github.klefstad_teaching.cs122b.movies.request.MovieSearchRequest;
import com.github.klefstad_teaching.cs122b.movies.request.PersonSearchRequest;
import com.github.klefstad_teaching.cs122b.movies.response.MovieMovieIDResponse;
import com.github.klefstad_teaching.cs122b.movies.response.MovieSearchResponse;
import com.github.klefstad_teaching.cs122b.movies.response.PersonPersonIDResponse;
import com.github.klefstad_teaching.cs122b.movies.response.PersonSearchResponse;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.github.klefstad_teaching.cs122b.core.result.MoviesResults;
import com.github.klefstad_teaching.cs122b.movies.data.Genre;
import com.github.klefstad_teaching.cs122b.movies.data.Movie;
import com.github.klefstad_teaching.cs122b.movies.data.MovieInfo;
import com.github.klefstad_teaching.cs122b.movies.data.MovieOrderBy;
import com.github.klefstad_teaching.cs122b.movies.data.Person;
import com.github.klefstad_teaching.cs122b.movies.data.PersonInfo;
import com.github.klefstad_teaching.cs122b.movies.data.PersonOrderBy;

@Component
public class MovieRepo {
    private final NamedParameterJdbcTemplate template;
    private final ObjectMapper objectMapper;

    @Autowired
    public MovieRepo(ObjectMapper objectMapper, NamedParameterJdbcTemplate template) {
        this.template = template;
        this.objectMapper = objectMapper;
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

    private final static String MOVIE_INFO_QUERY =

            "SELECT m.id, m.title, m.year, p.name, m.rating, m.num_Votes, m.budget, m.revenue, m.overview, m.backdrop_path, m.poster_path, m.hidden, "
                    +
                    "(SELECT json_arrayagg(JSON_OBJECT('id', g.id, 'name', g.name)) " +
                    "	FROM (SELECT DISTINCT g.id, g.name " +
                    "			FROM movies.genre as g " +
                    "			JOIN movies.movie_genre as mg on mg.genre_id = g.id " +
                    "			WHERE mg.movie_id = m.id " +
                    "			ORDER BY g.name " +
                    "		 ) as g " +
                    ") as genres " +
                    ",  " +
                    "(SELECT json_arrayagg(JSON_OBJECT('id', p.id, 'name', p.name)) " +
                    "    FROM (SELECT DISTINCT p.id, p.name, p.popularity " +
                    "			FROM movies.person as p " +
                    "			JOIN movies.movie_person as mp on mp.person_id = p.id " +
                    "			WHERE mp.movie_id = m.id " +
                    "			ORDER BY p.popularity DESC, p.id ASC " +
                    "		 ) as p " +
                    ") as persons " +
                    "FROM movies.movie as m " +
                    "JOIN movies.person as p on m.director_id = p.id ";

    private final static String PERSON_QUERY_EMPTY =

            "SELECT DISTINCT p.id, p.name, p.birthday, p.biography, p.birthplace, p.popularity, p.profile_path " +
                    "FROM movies.person as p ";

    private final static String PERSON_QUERY_NOT_EMPTY =

            "SELECT DISTINCT p.id, p.name, p.birthday, p.biography, p.birthplace, p.popularity, p.profile_path " +
                    "FROM movies.person as p " +
                    "JOIN movies.movie_person as mp on mp.person_id = p.id " +
                    "JOIN movies.movie as m on m.id = mp.movie_id ";

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

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<MovieMovieIDResponse> movieSearchMovieID(Long movieId) {

        try {
            MovieMovieIDResponse response = this.template.queryForObject(
                    MOVIE_INFO_QUERY + " Where m.id = :movieId ",
                    new MapSqlParameterSource().addValue("movieId", movieId, Types.INTEGER),
                    this::methodInsteadOfLambdaForMapping);

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (EmptyResultDataAccessException e) {
            MovieMovieIDResponse response = new MovieMovieIDResponse().setResult(MoviesResults.NO_MOVIE_WITH_ID_FOUND);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    private MovieMovieIDResponse methodInsteadOfLambdaForMapping(ResultSet rs, int rowNumber) throws SQLException {

        List<Genre> genres = null;

        try {
            String jsonArrayString = rs.getString("genres");

            Genre[] genreArray = objectMapper.readValue(jsonArrayString, Genre[].class);

            genres = Arrays.stream(genreArray).collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to Map 'genres' to Genre[]");
        }

        List<Person> persons = null;

        try {
            String jsonArrayString = rs.getString("persons");

            Person[] personArray = objectMapper.readValue(jsonArrayString, Person[].class);

            persons = Arrays.stream(personArray).collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to Map 'genres' to Genre[]");
        }

        MovieInfo movie = new MovieInfo()
                .setId(rs.getLong("m.id"))
                .setTitle(rs.getString("m.title"))
                .setYear(rs.getInt("m.year"))
                .setDirector(rs.getString("p.name"))
                .setRating(rs.getDouble("m.rating"))
                .setNumVotes(rs.getLong("m.num_Votes"))
                .setBudget(rs.getLong("m.budget"))
                .setRevenue(rs.getLong("m.revenue"))
                .setOverview(rs.getString("m.overview"))
                .setBackdropPath(rs.getString("m.backdrop_path"))
                .setPosterPath(rs.getString("m.poster_path"))
                .setHidden(rs.getBoolean("m.hidden"));

        return new MovieMovieIDResponse()
                .setMovies(movie)
                .setGenres(genres)
                .setPersons(persons);

    }

    @GetMapping("/person/search")
    public ResponseEntity<PersonSearchResponse> personSearch(@RequestBody PersonSearchRequest rq)
            throws ParseException {

        StringBuilder sql;
        MapSqlParameterSource source = new MapSqlParameterSource();
        boolean isWhereAlreadyAdded = false;

        if ((rq.getBirthday() == null && rq.getMovieTitle() == null && rq.getName() == null) ||
                (rq.getName() != null || rq.getBirthday() != null)) {

            sql = new StringBuilder(PERSON_QUERY_EMPTY);

            if (rq.getName() != null) {

                sql.append(" WHERE p.name LIKE :name");

                String wildcardSearch = '%' + rq.getName() + '%';

                source.addValue("name", wildcardSearch, Types.VARCHAR);
                isWhereAlreadyAdded = true;

            }
            if (rq.getBirthday() != null) {
                if (isWhereAlreadyAdded == true) {
                    sql.append(" AND p.birthday = :birthday");
                } else {
                    isWhereAlreadyAdded = true;
                    sql.append(" WHERE p.birthday = :birthday");
                }

                source.addValue("birthday", LocalDate.parse(rq.getBirthday()), Types.DATE);
            }
        } else {

            sql = new StringBuilder(PERSON_QUERY_NOT_EMPTY);

            if (rq.getName() != null) {

                sql.append(" WHERE p.name LIKE :name");

                String wildcardSearch = '%' + rq.getName() + '%';

                source.addValue("name", wildcardSearch, Types.VARCHAR);
                isWhereAlreadyAdded = true;

            }
            if (rq.getBirthday() != null) {
                if (isWhereAlreadyAdded == true) {
                    sql.append(" AND p.birthday = :birthday");
                } else {
                    isWhereAlreadyAdded = true;
                    sql.append(" WHERE p.birthday = :birthday");
                }

                source.addValue("birthday", LocalDate.parse(rq.getBirthday()), Types.DATE);
            }

            if (rq.getMovieTitle() != null) {

                if (isWhereAlreadyAdded == true) {
                    sql.append(" AND m.title LIKE :title");
                } else {
                    isWhereAlreadyAdded = true;
                    sql.append(" Where m.title LIKE :title");
                }
                String wildcardSearch = '%' + rq.getMovieTitle() + '%';
                source.addValue("title", wildcardSearch, Types.VARCHAR);
            }

        }
        PersonOrderBy orderBy = PersonOrderBy.fromString(rq.getOrderBy());
        sql.append(orderBy.toSql());
        sql.append(rq.getDirection().toString());
        sql.append(", p.id ASC");

        if (rq.getLimit() != null) {

            sql.append(" LIMIT :limit");
            source.addValue("limit", rq.getLimit(), Types.INTEGER);
        }

        if (rq.getPage() != null) {

            sql.append(" OFFSET :page");
            source.addValue("page", (rq.getPage() - 1) * rq.getLimit(), Types.INTEGER);
        }

        System.out.println(sql);

        List<PersonInfo> persons = this.template.query(
                sql.toString(),
                source,
                (rs, rowNum) -> new PersonInfo()
                        .setId(rs.getLong("p.id"))
                        .setName(rs.getString("p.name"))
                        .setBirthday(rs.getString("p.birthday"))
                        .setBiography(rs.getString("p.biography"))
                        .setBirthplace(rs.getString("p.birthplace"))
                        .setPopularity(rs.getFloat("p.popularity"))
                        .setProfilePath(rs.getString("p.profile_path")));

        PersonSearchResponse send = new PersonSearchResponse().setPersons(persons);
        return ResponseEntity.status(HttpStatus.OK).body(send);
    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<PersonPersonIDResponse> personSearchPersonID(Long personId) {

        StringBuilder sql;
        MapSqlParameterSource source = new MapSqlParameterSource();

        sql = new StringBuilder(PERSON_QUERY_EMPTY);
        sql.append(" Where p.id = :personId");
        source.addValue("personId", personId, Types.INTEGER);

        List<PersonInfo> person = this.template.query(
                sql.toString(),
                source,
                (rs, rowNum) -> new PersonInfo()
                        .setId(rs.getLong("p.id"))
                        .setName(rs.getString("p.name"))
                        .setBirthday(rs.getString("p.birthday"))
                        .setBiography(rs.getString("p.biography"))
                        .setBirthplace(rs.getString("p.birthplace"))
                        .setPopularity(rs.getFloat("p.popularity"))
                        .setProfilePath(rs.getString("p.profile_path")));

        if (person.isEmpty() == true) {
            PersonPersonIDResponse empty = new PersonPersonIDResponse()
                    .setResult(MoviesResults.NO_PERSON_WITH_ID_FOUND);
            return ResponseEntity.status(HttpStatus.OK).body(empty);
        }
        PersonPersonIDResponse send = new PersonPersonIDResponse()
                .setPerson(person.get(0))
                .setResult(MoviesResults.PERSON_WITH_ID_FOUND);

        return ResponseEntity.status(HttpStatus.OK).body(send);
    }
}
