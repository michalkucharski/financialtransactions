package com.mybank.transactions.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "transactions")
public class TransactionsData {

    @JsonProperty("transactionId")
    @Getter
    @Setter
    private String transactionId;

    @JsonProperty("net amount")
    @Getter
    @Setter
    private double netAmount;

    @JsonProperty("gross amount")
    @Getter
    @Setter
    private double grossAmount;

    @JsonProperty("payment type")
    @Getter
    @Setter
    private String paymentType;

    @JsonProperty("goods type")
    @Getter
    @Setter
    private String goodsType;

    @JsonProperty("tax category")
    @Getter
    @Setter
    private String taxCat;

    @JsonProperty("insert date")
    @Getter
    @Setter
    private String insertDate;

    @JsonProperty("update date")
    @Getter
    @Setter
    private String updateDate;
}
