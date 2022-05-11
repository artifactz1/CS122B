package com.github.klefstad_teaching.cs122b.billing.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.klefstad_teaching.cs122b.billing.data.Sale;
import com.github.klefstad_teaching.cs122b.core.result.Result;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListResponse {

    public Result result;
    public List<Sale> sales;

    public Result getResult() {
        return result;
    }

    public ListResponse setResult(Result result) {
        this.result = result;
        return this;
    }

    public List<Sale> getSales() {
        return sales;
    }

    public ListResponse setSales(List<Sale> sales) {
        this.sales = sales;
        return this;
    }

}
