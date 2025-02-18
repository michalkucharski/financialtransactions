package com.citibank.transactions.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class TransactionIn {

    private String transactionId;

    private double amount;

    private String paymentMethod;

    private String goodsType;

    private String taxCat;
}
