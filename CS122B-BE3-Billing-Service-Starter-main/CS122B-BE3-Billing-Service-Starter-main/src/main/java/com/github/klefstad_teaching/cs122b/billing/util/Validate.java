package com.github.klefstad_teaching.cs122b.billing.util;

import java.util.List;

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

    public boolean check(List<String> roles) {

        boolean checkPremium = false;
        for (int i = 0; i < roles.size(); i++) {
            if (roles.get(i).toUpperCase().equals("PREMIUM")) {
                checkPremium = true;
            }
        }
        return checkPremium;
    }

}
