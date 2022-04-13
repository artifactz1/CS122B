package com.github.klefstad_teaching.cs122b.idm.repo;

import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.util.List;

import com.github.klefstad_teaching.cs122b.core.error.ResultError;
import com.github.klefstad_teaching.cs122b.core.result.IDMResults;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.RefreshToken;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.User;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.type.TokenStatus;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.type.UserStatus;
import com.github.klefstad_teaching.cs122b.idm.reponse.LoginResponse;
import com.github.klefstad_teaching.cs122b.idm.reponse.RefreshResponse;
import com.github.klefstad_teaching.cs122b.idm.reponse.RefreshTKResponse;
import com.github.klefstad_teaching.cs122b.idm.reponse.RegisterResponse;
import com.github.klefstad_teaching.cs122b.idm.request.LoginRequest;
import com.github.klefstad_teaching.cs122b.idm.request.RefreshRequest;
import com.github.klefstad_teaching.cs122b.idm.request.RefreshTKRequest;
import com.github.klefstad_teaching.cs122b.idm.request.RegisterRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
public class IDMRepo {

    private final NamedParameterJdbcTemplate template;

    @Autowired
    public IDMRepo(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody RegisterRequest vars) {

        User user = vars.getUser();

        try {
            int rowsUpdated = this.template.update(
                    "INSERT INTO idm.user(email, user_status_id, salt, hashed_password)" +
                            "VALUES (:email, :user_status_id, :salt, :hashed_password);",
                    new MapSqlParameterSource()
                            .addValue("email", user.getEmail(), Types.VARCHAR)
                            .addValue("user_status_id", user.getUserStatus().id(), Types.INTEGER)
                            .addValue("salt", user.getSalt(), Types.CHAR)
                            .addValue("hashed_password", user.getHashedPassword(), Types.CHAR));

            if (rowsUpdated > 0) {
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        } catch (Exception e) {
            throw new ResultError(IDMResults.USER_ALREADY_EXISTS);
        }

    }

    @PostMapping("/login1")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest vars) {

        String emailInput = vars.getEmail();
        List<User> users = this.template.query(
                "SELECT id, email, user_status_id, salt, hashed_password " +
                        "FROM idm.user " +
                        "WHERE email = :emailInput;",

                new MapSqlParameterSource().addValue("emailInput", emailInput, Types.VARCHAR),

                (rs, rowNum) -> new User()
                        .setId(rs.getInt("id"))
                        .setEmail(rs.getString("email"))
                        .setUserStatus(UserStatus.fromId(rs.getInt("user_status_id")))
                        .setSalt(rs.getString("salt"))
                        .setHashedPassword(rs.getString("hashed_password")));

        LoginResponse response = new LoginResponse().setUsers(users);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    // for build

    @PostMapping("/insertRefresh")
    public ResponseEntity<LoginResponse> insertRefresh(
            @RequestBody LoginRequest vars) {

        RefreshToken rTK = vars.getRefreshToken();

        int rowsUpdated = this.template.update(
                "INSERT INTO idm.refresh_token (token, user_id, token_status_id, expire_time, max_life_time)"
                        +
                        "VALUES (:token, :user_id, :token_status_id, :expire_time, :max_life_time);",
                new MapSqlParameterSource()
                        .addValue("token", rTK.getToken(), Types.CHAR)
                        .addValue("user_id", rTK.getUserId(), Types.INTEGER)
                        .addValue("token_status_id", rTK.getTokenStatus().id(), Types.INTEGER)
                        .addValue("expire_time", Timestamp.from(rTK.getExpireTime()), Types.TIMESTAMP)
                        .addValue("max_life_time", Timestamp.from(rTK.getMaxLifeTime()), Types.TIMESTAMP));

        if (rowsUpdated > 0) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/getUserfromRefresh")
    public ResponseEntity<RefreshResponse> getUserfromRefresh(

            @RequestBody RefreshTKRequest vars) {

        Integer userID = vars.getRefreshToken().getUserId();

        List<User> users = this.template.query(
                "SELECT id, email, user_status_id, salt, hashed_password " +
                        "FROM idm.user " +
                        "WHERE id = :userID;",

                new MapSqlParameterSource().addValue("userID", userID, Types.INTEGER),

                (rs, rowNum) -> new User()
                        .setId(rs.getInt("id"))
                        .setEmail(rs.getString("email"))
                        .setUserStatus(UserStatus.fromId(rs.getInt("user_status_id")))
                        .setSalt(rs.getString("salt"))
                        .setHashedPassword(rs.getString("hashed_password")));

        RefreshResponse response = new RefreshResponse().setUser(users.get(0));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/RefreshTokenDB")
    public ResponseEntity<RefreshTKResponse> getRefreshTokenDB(

            @RequestBody RefreshRequest vars) {

        String token = vars.getRefreshToken();

        List<RefreshToken> tokens = this.template.query(
                "SELECT id, token, user_id, token_status_id, expire_time, max_life_time " +
                        "FROM idm.refresh_token " +
                        "WHERE token = :token;",

                new MapSqlParameterSource().addValue("token", token, Types.CHAR),

                (rs, rowNum) -> new RefreshToken()
                        .setId(rs.getInt("id"))
                        .setToken(rs.getString("token"))
                        .setUserId(rs.getInt("user_id"))
                        .setTokenStatus(TokenStatus.fromId(rs.getInt("token_status_id")))
                        .setExpireTime(rs.getTimestamp("expire_time").toInstant())
                        .setMaxLifeTime(rs.getTimestamp("max_life_time").toInstant()));

        if (tokens.isEmpty() == true) {
            throw new ResultError(IDMResults.REFRESH_TOKEN_NOT_FOUND);
        }

        RefreshTKResponse response = new RefreshTKResponse().setRefreshToken(tokens.get(0));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/expreRFDB")
    public ResponseEntity<RefreshResponse> expreRFDB(

            @RequestBody RefreshTKRequest vars) {

        RefreshToken rTK = vars.getRefreshToken();
        Integer tokenID = rTK.getTokenStatus().id();
        Integer userID = rTK.getUserId();

        int rowsUpdated = this.template.update(

                "UPDATE idm.refresh_token " +
                        "SET token_status_id = :tokenID " +
                        "WHERE user_id = :userID;",

                new MapSqlParameterSource()
                        .addValue("userID", userID, Types.INTEGER)
                        .addValue("tokenID", tokenID, Types.INTEGER));

        if (rowsUpdated > 0) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/updateRefreshTKET")
    public ResponseEntity<RefreshResponse> updateRefreshTKET(
            @RequestBody RefreshTKRequest vars) {

        RefreshToken rTK = vars.getRefreshToken();
        Instant expire = rTK.getExpireTime();
        Integer userID = rTK.getUserId();

        int rowsUpdated = this.template.update(

                "UPDATE idm.refresh_token " +
                        "SET expire_time = :expire " +
                        "WHERE user_id = :userID;",

                new MapSqlParameterSource()
                        .addValue("userID", userID, Types.INTEGER)
                        .addValue("expire", Timestamp.from(expire), Types.TIMESTAMP));

        if (rowsUpdated > 0) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

}
