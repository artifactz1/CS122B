package com.github.klefstad_teaching.cs122b.idm.repo;

import java.sql.Types;

import com.github.klefstad_teaching.cs122b.idm.repo.entity.User;
import com.github.klefstad_teaching.cs122b.idm.reponse.RegisterResponse;
import com.github.klefstad_teaching.cs122b.idm.request.RegisterRequest;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

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
                "INSERT INTO idm.user(id, email, user_status_id, salt, hashed_password)" +
                        "VALUES (:id, :email, :user_status, :salt, :hashed_password);",
                new MapSqlParameterSource()
                        .addValue("id", user.getId(), Types.INTEGER)
                        .addValue("email", user.getEmail(), Types.VARCHAR)
                        .addValue("user_status_id", user.getUserStatus(), Types.INTEGER)
                        .addValue("salt", user.getSalt(), Types.CHAR)
                        .addValue("hashed_password", user.getHashedPassword(), Types.CHAR));

        if (rowsUpdated > 0) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

}
