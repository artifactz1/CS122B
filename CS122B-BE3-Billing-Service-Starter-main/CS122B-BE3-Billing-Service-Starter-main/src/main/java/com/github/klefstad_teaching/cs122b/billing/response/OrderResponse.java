package com.github.klefstad_teaching.cs122b.billing.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.klefstad_teaching.cs122b.core.result.Result;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {

    public Result result;
    public String paymentIntentId;
    public String clientSecret;

    public Result getResult() {
        return result;
    }

    public OrderResponse setResult(Result result) {
        this.result = result;
        return this;
    }

    public String getPaymentIntentId() {
        return paymentIntentId;
    }

    public OrderResponse setPaymentIntentId(String paymentIntentId) {
        this.paymentIntentId = paymentIntentId;
        return this;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public OrderResponse setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

}
