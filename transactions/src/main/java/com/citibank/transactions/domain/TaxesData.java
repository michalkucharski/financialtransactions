package com.citibank.transactions.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "taxes")
public class TaxesData {

    @JsonProperty("tax ID")
    @Getter
    @Setter
    private String taxId;

    @JsonProperty("type of tax")
    @Getter
    @Setter
    private String taxType;

    @JsonProperty("tax value")
    @Getter
    @Setter
    private double taxValue;

    @JsonProperty("insert date")
    @Getter
    @Setter
    private String insertDate;

    @JsonProperty("update date")
    @Getter
    @Setter
    private String updateDate;
}
