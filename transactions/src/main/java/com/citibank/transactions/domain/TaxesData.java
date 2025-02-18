package com.citibank.transactions.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "taxes")
public class TaxesData {

//    private String taxId;

    private String taxCat;

    private double taxValue;

    private String insertDate;

    private String updateDate;
}
