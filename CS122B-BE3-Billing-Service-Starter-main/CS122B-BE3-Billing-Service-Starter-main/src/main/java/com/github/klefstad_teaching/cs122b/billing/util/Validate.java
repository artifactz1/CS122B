package com.github.klefstad_teaching.cs122b.billing.util;

import com.github.klefstad_teaching.cs122b.billing.request.CartInsertRequest;
import com.github.klefstad_teaching.cs122b.core.error.ResultError;
import com.github.klefstad_teaching.cs122b.core.result.BillingResults;

import org.springframework.stereotype.Component;

@Component
public final class Validate {
    public void check(Integer q) {

        if (q <= 0) {
            throw new ResultError(BillingResults.INVALID_QUANTITY);
        }
        if (q > 10) {
            throw new ResultError(BillingResults.MAX_QUANTITY);
        }
    }

}
