package com.github.klefstad_teaching.cs122b.billing.request;

public class CompleteRequest {

    String paymentIntentId;

    public String getPaymentIntentId() {
        return paymentIntentId;
    }

    public CompleteRequest setPaymentIntentId(String paymentIntentId) {
        this.paymentIntentId = paymentIntentId;
        return this;
    }

}
