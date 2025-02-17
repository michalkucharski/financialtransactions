package com.citibank.transactions.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class TransactionIn {

    @JsonProperty("transactionId")
    private String transactionId;

    @JsonProperty("amount")
    private double amount;

    @JsonProperty("payment method")
    private String paymentMethod;

    @JsonProperty("goods type")
    private String goodsType;
}
