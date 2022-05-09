package com.github.klefstad_teaching.cs122b.billing.repo;

import java.sql.Types;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.klefstad_teaching.cs122b.billing.BillingService;
import com.github.klefstad_teaching.cs122b.billing.request.CartRequest;
import com.github.klefstad_teaching.cs122b.billing.response.CartResponse;
import com.github.klefstad_teaching.cs122b.core.error.ResultError;
import com.github.klefstad_teaching.cs122b.core.result.BillingResults;
import com.github.klefstad_teaching.cs122b.core.result.IDMResults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component

public class BillingRepo {

    private final NamedParameterJdbcTemplate template;

    @Autowired
    public BillingRepo(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    private final static String CART_INSERT = "INSERT INTO billing.cart(user_id, movie_id, quantity) " +
            "VALUES (:user_id, :movie_id, :quantity); ";

    @PostMapping("/cart/insert")
    public ResponseEntity<CartResponse> insertCart(@RequestBody CartRequest rq, Long userID) {

        try {
            int rowsUpdated = this.template.update(CART_INSERT,
                    new MapSqlParameterSource()
                            .addValue("user_id", userID, Types.INTEGER)
                            .addValue("movie_id", rq.getMovieId(), Types.INTEGER)
                            .addValue("quantity", rq.getQuantity(), Types.INTEGER));

            if (rowsUpdated > 0) {
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        } catch (Exception e) {
            throw new ResultError(BillingResults.CART_ITEM_EXISTS);
        }

    }

    private final static String CART_UPDATE = "UPDATE billing.cart " +
            "SET quantity = :quantity " +
            "WHERE user_id = :user_id AND movie_id = :movie_id; ";

    @PostMapping("/cart/update")
    public ResponseEntity<CartResponse> updateCart(@RequestBody CartRequest rq, Long userID) {

        int rowsUpdated = this.template.update(CART_UPDATE,
                new MapSqlParameterSource()
                        .addValue("user_id", userID, Types.INTEGER)
                        .addValue("movie_id", rq.getMovieId(), Types.INTEGER)
                        .addValue("quantity", rq.getQuantity(), Types.INTEGER));

        if (rowsUpdated > 0) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            throw new ResultError(BillingResults.CART_ITEM_DOES_NOT_EXIST);
        }
    }

    private final static String CART_DELETE = "DELETE from billing.cart " +
            "WHERE movie_id = :movie_id AND user_id = :user_id; ";

    @DeleteMapping("/cart/delete/{movieId}")
    public ResponseEntity<CartResponse> deleteCart(Long movie_id, Long userID) {

        int rowsUpdated = this.template.update(CART_DELETE,
                new MapSqlParameterSource()
                        .addValue("user_id", userID, Types.INTEGER)
                        .addValue("movie_id", movie_id, Types.INTEGER));

        if (rowsUpdated > 0) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {

            throw new ResultError(BillingResults.CART_ITEM_DOES_NOT_EXIST);
        }
    }
}
