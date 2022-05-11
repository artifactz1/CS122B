package com.github.klefstad_teaching.cs122b.billing.data;

import java.math.BigDecimal;
import java.time.Instant;

public class Sale {

    public Long saleId;
    public BigDecimal total;
    public Instant orderDate;

    public Long getSaleId() {
        return saleId;
    }

    public Sale setSaleId(Long saleId) {
        this.saleId = saleId;
        return this;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public Sale setTotal(BigDecimal total) {
        this.total = total;
        return this;
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public Sale setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
        return this;
    }

}
