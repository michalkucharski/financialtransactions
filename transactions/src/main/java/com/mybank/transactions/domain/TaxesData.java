package com.mybank.transactions.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "taxes")
public class TaxesData {

//    private String taxId;

    private String taxCat;

    private double taxValue;

    private String insertDate;

    private String updateDate;


}
