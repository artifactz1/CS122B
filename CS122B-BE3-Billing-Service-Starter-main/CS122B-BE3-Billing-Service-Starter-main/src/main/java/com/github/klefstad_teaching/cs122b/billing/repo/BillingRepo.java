package com.github.klefstad_teaching.cs122b.billing.repo;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.klefstad_teaching.cs122b.billing.BillingService;
import com.github.klefstad_teaching.cs122b.billing.data.Item;
import com.github.klefstad_teaching.cs122b.billing.request.CartRequest;
import com.github.klefstad_teaching.cs122b.billing.response.CartResponse;
import com.github.klefstad_teaching.cs122b.billing.response.RetrieveResponse;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

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

    private final static String CART_RETRIEVE =

            "SELECT mp.unit_price, mp.premium_discount, b.quantity, b.movie_id, m.title, m.backdrop_path, m.poster_path "
                    +
                    "FROM billing.cart as b " +
                    "JOIN movies.movie as m on m.id = b.movie_id " +
                    "JOIN billing.movie_price as mp on mp.movie_id = m.id " +
                    "WHERE b.user_id = :user_id ";

    @GetMapping("/cart/retrieve")
    public ResponseEntity<RetrieveResponse> retrieveCart(Long user_id, boolean checkPremium) {

        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("user_id", user_id, Types.INTEGER);

        List<Item> items = this.template.query(
                CART_RETRIEVE,
                source,
                (rs, rowNum) -> new Item()
                        .setUnitPrice(
                                (checkPremium == true)
                                        ? (calculateDiscount((rs.getBigDecimal("mp.unit_price")),
                                                rs.getInt("mp.premium_discount")))
                                        : rs.getBigDecimal("mp.unit_price"))
                        .setQuantity(rs.getInt("b.quantity"))
                        .setMovieId(rs.getLong("b.movie_id"))
                        .setMovieTitle(rs.getString("m.title"))
                        .setBackdropPath(rs.getString("m.backdrop_path"))
                        .setPosterPath(rs.getString("m.poster_path")));

        Item[] arrayItems = new Item[items.size()];
        items.toArray(arrayItems);

        if (items.size() == 0) {

            RetrieveResponse send = new RetrieveResponse()
                    .setResult(BillingResults.CART_EMPTY);
            return ResponseEntity.status(HttpStatus.OK).body(send);

        }

        RetrieveResponse send = new RetrieveResponse()
                .setItems(arrayItems)
                .setResult(BillingResults.CART_RETRIEVED)
                .setTotal(calculateTotalCart(items));
        return ResponseEntity.status(HttpStatus.OK).body(send);

    }

    public BigDecimal calculateDiscount(BigDecimal unitPrice, Integer discount) {

        BigDecimal DiscountedUnitPrice = unitPrice.multiply(BigDecimal.valueOf(1 - (discount / 100.0)));
        return DiscountedUnitPrice;
    }

    public BigDecimal calculateTotalCart(List<Item> items) {

        BigDecimal total = new BigDecimal(0.0);

        for (int i = 0; i < items.size(); i++) {

            BigDecimal uPrice = items.get(i).getUnitPrice();
            Integer quantity = items.get(i).getQuantity();
            total.add(uPrice.multiply(BigDecimal.valueOf(quantity)));
        }

        return total;
    }
}
