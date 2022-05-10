package com.github.klefstad_teaching.cs122b.billing.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.klefstad_teaching.cs122b.billing.data.Item;
import com.github.klefstad_teaching.cs122b.core.result.Result;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RetrieveResponse {

    public Result result;
    public BigDecimal total;
    public Item[] items;

    public Result getResult() {
        return result;
    }

    public RetrieveResponse setResult(Result result) {
        this.result = result;
        return this;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public RetrieveResponse setTotal(BigDecimal total) {
        this.total = total;
        return this;
    }

    public Item[] getItems() {
        return items;
    }

    public RetrieveResponse setItems(Item[] items) {
        this.items = items;
        return this;
    }
}
