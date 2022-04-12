package com.github.klefstad_teaching.cs122b.idm.repo;

import java.sql.Types;
import java.util.List;

import com.github.klefstad_teaching.cs122b.idm.repo.entity.RefreshToken;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.User;
import com.github.klefstad_teaching.cs122b.idm.repo.entity.type.UserStatus;
import com.github.klefstad_teaching.cs122b.idm.reponse.LoginResponse;
import com.github.klefstad_teaching.cs122b.idm.reponse.RefreshResponse;
import com.github.klefstad_teaching.cs122b.idm.reponse.RegisterResponse;
import com.github.klefstad_teaching.cs122b.idm.request.LoginRequest;
import com.github.klefstad_teaching.cs122b.idm.request.RefreshRequest;
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
    }

    @PostMapping("/login")
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> insertRefresh(
            @RequestBody LoginRequest vars) {

        RefreshToken rTK = vars.getRefreshToken();

        int rowsUpdated = this.template.update(
                "INSERT INTO idm.refresh_token (id, token, user_id, token_status_id, expire_time, max_life_time)"
                        +
                        "VALUES (:id, :token, :user_id, :token_status_id, :expire_time, :max_life_time);",
                new MapSqlParameterSource()
                        .addValue("id", rTK.getId(), Types.INTEGER)
                        .addValue("token", rTK.getToken(), Types.CHAR)
                        .addValue("user_id", rTK.getUserId(), Types.INTEGER)
                        .addValue("token_status_id", rTK.getTokenStatus().id(), Types.INTEGER)
                        .addValue("expire_time", rTK.getExpireTime(), Types.TIMESTAMP)
                        .addValue("max_life_time", rTK.getMaxLifeTime(), Types.TIMESTAMP));

        if (rowsUpdated > 0) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> getUserfromRefresh(

            @RequestBody RefreshRequest vars) {

        Integer userID = vars.getRefreshToken().getId();

        List<User> users = this.template.query(
                "SELECT id, email, user_status_id, salt, hashed_password " +
                        "FROM idm.user " +
                        "WHERE id = :userID;",

                new MapSqlParameterSource().addValue("emailInput", userID, Types.INTEGER),

                (rs, rowNum) -> new User()
                        .setId(rs.getInt("id"))
                        .setEmail(rs.getString("email"))
                        .setUserStatus(UserStatus.fromId(rs.getInt("user_status_id")))
                        .setSalt(rs.getString("salt"))
                        .setHashedPassword(rs.getString("hashed_password")));

        RefreshResponse response = new RefreshResponse().setUser(users.get(0));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
