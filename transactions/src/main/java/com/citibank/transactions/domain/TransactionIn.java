package com.citibank.transactions.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class TransactionIn {

    @JsonProperty("transaction")
    @Getter
    @Setter
    private TransactionsData transactionsData;
}
