package com.github.klefstad_teaching.cs122b.billing.repo;

import java.sql.Types;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.klefstad_teaching.cs122b.billing.BillingService;
import com.github.klefstad_teaching.cs122b.billing.request.CartInsertRequest;
import com.github.klefstad_teaching.cs122b.billing.response.CartInsertResponse;
import com.github.klefstad_teaching.cs122b.core.error.ResultError;
import com.github.klefstad_teaching.cs122b.core.result.BillingResults;
import com.github.klefstad_teaching.cs122b.core.result.IDMResults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component

public class BillingRepo {

    private final NamedParameterJdbcTemplate template;
    private final static String CART_INSERT = "INSERT INTO billing.cart(user_id, movie_id, quantity)" +
            " VALUES (:user_id, :movie_id, :quantity); ";

    @Autowired
    public BillingRepo(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @PostMapping("/cart/insert")
    public ResponseEntity<CartInsertResponse> insertCart(@RequestBody CartInsertRequest rq, Long userID) {

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
}
