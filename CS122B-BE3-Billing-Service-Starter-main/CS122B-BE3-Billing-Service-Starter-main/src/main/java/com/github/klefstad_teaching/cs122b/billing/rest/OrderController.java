package com.github.klefstad_teaching.cs122b.billing.rest;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;

import com.github.klefstad_teaching.cs122b.billing.data.Item;
import com.github.klefstad_teaching.cs122b.billing.repo.BillingRepo;
import com.github.klefstad_teaching.cs122b.billing.request.CartRequest;
import com.github.klefstad_teaching.cs122b.billing.request.CompleteRequest;
import com.github.klefstad_teaching.cs122b.billing.response.CartResponse;
import com.github.klefstad_teaching.cs122b.billing.response.CompleteResponse;
import com.github.klefstad_teaching.cs122b.billing.response.OrderResponse;
import com.github.klefstad_teaching.cs122b.billing.response.RetrieveResponse;
import com.github.klefstad_teaching.cs122b.billing.util.Validate;
import com.github.klefstad_teaching.cs122b.core.error.ResultError;
import com.github.klefstad_teaching.cs122b.core.result.BillingResults;
import com.github.klefstad_teaching.cs122b.core.security.JWTManager;
import com.nimbusds.jwt.SignedJWT;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    private final BillingRepo repo;
    private final Validate validate;
    // private static final Logger LOG =
    // LoggerFactory.getLogger(OrderController.class);

    @Autowired
    public OrderController(BillingRepo repo, Validate validate) {
        this.repo = repo;
        this.validate = validate;
    }

    @GetMapping("/order/payment")
    public ResponseEntity<OrderResponse> orderpayment(@AuthenticationPrincipal SignedJWT user)
            throws ParseException {

        try {

            List<String> roles = user.getJWTClaimsSet().getStringListClaim(JWTManager.CLAIM_ROLES);
            boolean checkPremium = validate.check(roles);
            Long uID = user.getJWTClaimsSet().getLongClaim(JWTManager.CLAIM_ID);
            ResponseEntity<RetrieveResponse> response = repo.retrieveCart(uID, checkPremium);
            RetrieveResponse rs = response.getBody();

            if (rs.getResult().equals(BillingResults.CART_EMPTY)) {
                OrderResponse send = new OrderResponse()
                        .setResult(BillingResults.CART_EMPTY);
                return ResponseEntity.status(HttpStatus.OK).body(send);
            }

            List<Item> items = Arrays.asList(rs.getItems());
            String movies = "";

            for (Item s : items) {
                movies = movies + s.getMovieTitle() + ",";
            }

            Long amountInTotalCents = rs.getTotal().longValue() * 100;
            String description = movies;
            String userId = Long.toString(uID);

            PaymentIntentCreateParams paymentIntentCreateParams = PaymentIntentCreateParams
                    .builder()
                    .setCurrency("USD") // This will always be the same for our project
                    .setDescription(description)
                    .setAmount(amountInTotalCents)
                    // We use MetaData to keep track of the user that should pay for the order
                    .putMetadata("userId", userId.toString())
                    .setAutomaticPaymentMethods(
                            // This will tell stripe to generate the payment methods automatically
                            // This will always be the same for our project
                            PaymentIntentCreateParams.AutomaticPaymentMethods
                                    .builder()
                                    .setEnabled(true)
                                    .build())
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(paymentIntentCreateParams);

            String paymentIntentId = paymentIntent.getId();
            // LOG.info("PaymentIntent ID: {}", paymentIntentId);
            // LOG.info("Client Secret: {}", paymentIntent.getClientSecret());
            // PaymentIntent retrievedPaymentIntent =
            // PaymentIntent.retrieve(paymentIntentId);
            // LOG.info("Current Status: {}", retrievedPaymentIntent.getStatus());

            OrderResponse body = new OrderResponse()
                    .setResult(BillingResults.ORDER_PAYMENT_INTENT_CREATED)
                    .setPaymentIntentId(paymentIntentId)
                    .setClientSecret(paymentIntent.getClientSecret());

            return ResponseEntity.status(HttpStatus.OK)
                    .body(body);
        } catch (StripeException e) {
            throw new ResultError(BillingResults.STRIPE_ERROR);
        }
    }

    @PostMapping("/order/complete")
    public ResponseEntity<CompleteResponse> ordercomplete(@AuthenticationPrincipal SignedJWT user,
            @RequestBody CompleteRequest rq) throws ParseException {

        Long userID = user.getJWTClaimsSet().getLongClaim(JWTManager.CLAIM_ID);

        try {
            PaymentIntent pI = PaymentIntent.retrieve(rq.getPaymentIntentId());

            if (pI.getMetadata().get("userId").equals(userID.toString())) {
                if (pI.getStatus().toUpperCase().equals("SUCCEEDED")) {

                    List<String> roles = user.getJWTClaimsSet().getStringListClaim(JWTManager.CLAIM_ROLES);
                    boolean checkPremium = validate.check(roles);
                    ResponseEntity<RetrieveResponse> response = repo.retrieveCart(userID, checkPremium);
                    RetrieveResponse rs = response.getBody();
                    List<Item> items = Arrays.asList(rs.getItems());

                    // Once Successful clear cart
                    repo.clearcart(userID);
                    ResponseEntity<CompleteResponse> res = repo.completeOrder(userID, checkPremium, items,
                            rs.getTotal());
                    return res;

                } else {
                    throw new ResultError(BillingResults.ORDER_CANNOT_COMPLETE_NOT_SUCCEEDED);
                }
            } else {
                throw new ResultError(BillingResults.ORDER_CANNOT_COMPLETE_WRONG_USER);
            }
        } catch (StripeException e) {
            throw new ResultError(BillingResults.STRIPE_ERROR);
        }
    }

}
