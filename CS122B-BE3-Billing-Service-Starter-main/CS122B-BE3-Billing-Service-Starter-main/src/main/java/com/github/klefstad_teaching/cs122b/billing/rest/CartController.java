package com.github.klefstad_teaching.cs122b.billing.rest;

import java.text.ParseException;
import java.util.List;

import com.github.klefstad_teaching.cs122b.billing.repo.BillingRepo;
import com.github.klefstad_teaching.cs122b.billing.request.CartRequest;
import com.github.klefstad_teaching.cs122b.billing.response.CartResponse;
import com.github.klefstad_teaching.cs122b.billing.util.Validate;
import com.github.klefstad_teaching.cs122b.core.result.BillingResults;
import com.github.klefstad_teaching.cs122b.core.security.JWTManager;
import com.nimbusds.jwt.SignedJWT;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {
    private final BillingRepo repo;
    private final Validate validate;

    @Autowired
    public CartController(BillingRepo repo, Validate validate) {
        this.repo = repo;
        this.validate = validate;
    }

    @PostMapping("/cart/insert")
    public ResponseEntity<CartResponse> cartinsert(@AuthenticationPrincipal SignedJWT user,
            @RequestBody CartRequest rq) throws ParseException {

        Long userID = user.getJWTClaimsSet().getLongClaim(JWTManager.CLAIM_ID);

        validate.check(rq.getQuantity());
        repo.insertCart(rq, userID);

        CartResponse good = new CartResponse()
                .setResult(BillingResults.CART_ITEM_INSERTED);

        return ResponseEntity.status(HttpStatus.OK).body(good);
    }

    @PostMapping("/cart/update")
    public ResponseEntity<CartResponse> cartupdate(@AuthenticationPrincipal SignedJWT user,
            @RequestBody CartRequest rq) throws ParseException {

        Long userID = user.getJWTClaimsSet().getLongClaim(JWTManager.CLAIM_ID);

        validate.check(rq.getQuantity());
        repo.updateCart(rq, userID);

        CartResponse good = new CartResponse()
                .setResult(BillingResults.CART_ITEM_UPDATED);

        return ResponseEntity.status(HttpStatus.OK).body(good);
    }

    @DeleteMapping("/cart/delete/{movieId}")
    public ResponseEntity<CartResponse> cartdelete(@AuthenticationPrincipal SignedJWT user,
            @PathVariable Long movieId) throws ParseException {

        Long userID = user.getJWTClaimsSet().getLongClaim(JWTManager.CLAIM_ID);
        repo.deleteCart(movieId, userID);

        CartResponse good = new CartResponse()
                .setResult(BillingResults.CART_ITEM_DELETED);

        return ResponseEntity.status(HttpStatus.OK).body(good);

    }

}
