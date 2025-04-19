package com.mybank.transactions.controller;

import com.mybank.transactions.domain.TaxIn;
import com.mybank.transactions.domain.TaxesData;
import com.mybank.transactions.exceptions.InvalidRequestException;
import com.mybank.transactions.exceptions.TaxNotFoundException;
import com.mybank.transactions.service.TaxServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path="/taxes/v1")
public class TaxController {

    @Autowired
    TaxServiceImpl taxService;

    @Operation(summary = "Submit new tax")
    @PostMapping(path="/submitTax")
    public ResponseEntity<String> submitTax(@RequestBody TaxIn taxIn) throws InvalidRequestException {

        if (taxIn == null) {
            throw new InvalidRequestException("No data to submit");
        }

        if (taxIn.getTaxCat().isEmpty()) {
            throw new InvalidRequestException("Tax category is not informed");
        }

        if (taxIn.getTaxValue() < 0.1) {
            throw new InvalidRequestException("The minimal tax rate is 0.1");
        }

        try {
            taxService.submitNewTax(taxIn);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }

        return ResponseEntity.accepted().body("The new tax was submitted successfully");
    }

    @Operation(summary = "Get a tax by its id/category")
    @GetMapping(path="/retrieveTax/{id}")
    public ResponseEntity<?> retrieveTax(@PathVariable("id") String taxId) throws InvalidRequestException, TaxNotFoundException {

        TaxesData taxOut = new TaxesData();
        if (taxId.isEmpty()) {
            throw new InvalidRequestException("TaxId is not informed ");
        }

        try {
            String taxIdUp = taxId.toUpperCase();
            taxOut = taxService.findTaxById(taxId);

            if (taxOut == null) {
                throw new TaxNotFoundException("Tax ist not founded");
            }
        } catch (TaxNotFoundException et) {
            throw et;
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }

        return ResponseEntity.ok().body(taxOut);

    }

    @Operation(summary = "Retrieve all taxes")
    @GetMapping(path="/retrieveTax")
    public ResponseEntity<?> retrieveAllTaxes() {

        List<TaxesData> trxOut;



        try {
            trxOut = taxService.findAllTaxes();

        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }

        return ResponseEntity.ok().body(trxOut);

    }
}
