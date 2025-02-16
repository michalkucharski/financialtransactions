package com.citibank.transactions.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "taxes")
public class TaxesData {

    @JsonProperty("tax ID")
    private String taxId;

    @JsonProperty("category of tax")
    private String taxCat;

    @JsonProperty("tax value")
    private double taxValue;

    @JsonProperty("insert date")
    private String insertDate;

    @JsonProperty("update date")
    private String updateDate;
}
