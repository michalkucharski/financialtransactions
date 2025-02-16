package com.citibank.transactions.controller;

import com.citibank.transactions.domain.*;
import com.citibank.transactions.enums.PaymentMethods;
import com.citibank.transactions.exceptions.InvalidRequestException;
import com.citibank.transactions.exceptions.TaxNotFoundException;
import com.citibank.transactions.exceptions.TransactionNotFoundException;
import com.citibank.transactions.service.TaxServiceImpl;
import com.citibank.transactions.service.TransactionsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path="/transactions/v1")
public class TransactionController {

    @Autowired
    TransactionsServiceImpl transactionsService;

    @Autowired
    TaxServiceImpl taxService;

    @PostMapping(path="/submitTransaction")
    public ResponseEntity<?> submitTransaction(@RequestBody TransactionIn transactionIn) throws InvalidRequestException {

        if (transactionIn == null) {
            throw new InvalidRequestException("No data to submit");
        }

        if (transactionIn.getTransactionId().isEmpty()) {
            throw new InvalidRequestException("Transaction Id is not informed");
        }

        if (transactionIn.getAmount() < 1 ) {
            throw new InvalidRequestException("Transaction amount should be more than 0");
        }

        if (transactionIn.getPaymentMethod().isEmpty()) {
            throw new InvalidRequestException("Payment method should be informed");
        }

        if(transactionIn.getGoodsType().isEmpty()) {
            throw new InvalidRequestException("Type of goods should be informed");
        }


        if (!transactionIn.getPaymentMethod().toUpperCase().contains(PaymentMethods.values().toString())) {
            throw new InvalidRequestException("Payment methods is not allowed");
        }
//        final TipoTrx output = TipoTrx.valueOf(tipofinal);
//        setTipo(output.toString());

        try {
            transactionsService.submitTransaction(transactionIn);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }

    }

    @GetMapping(path="/retrieveTransaction/{id}")
    public ResponseEntity<TransactionsData> retrieveTrx(@PathVariable("id") String transactionId) throws InvalidRequestException, TransactionNotFoundException {

        var trxOut = new TransactionsData();
        if (transactionId == null | transactionId.isEmpty()) {
            throw new InvalidRequestException("TransactionId is not informed ");
        }

        trxOut = transactionsService.findTransactionyId(transactionId);

        if (trxOut == null) {
            throw new TransactionNotFoundException("Transaction ist not founded");
        }
        return ResponseEntity.ok().body(trxOut);

    }

    @GetMapping(path="/retrieveTransaction")
    public ResponseEntity<?> retrieveAllTrx() throws TransactionNotFoundException {

        List<TransactionsData> trxOut;

        try {
            trxOut = transactionsService.findAllTransactions();

            if (trxOut == null) {
                throw new TransactionNotFoundException("Transaction ist not founded");
            }
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }

        return ResponseEntity.ok().body(trxOut);

    }

    @PostMapping(path="/submitTax")
    public ResponseEntity<String> submitTax(@RequestBody TaxIn taxIn) throws InvalidRequestException {

        if (taxIn == null) {
            throw new InvalidRequestException("No data to submit");
        }

        if (taxIn.getTaxCat().isEmpty()) {
            throw new InvalidRequestException("Tax category is not informmed");
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

    @GetMapping(path="/retrieveTax/{id}")
    public ResponseEntity<?> retrieveTax(@PathVariable("id") String taxId) throws InvalidRequestException, TaxNotFoundException {

        var taxOut = new TaxesData();
        if (taxId == null ) {
            throw new InvalidRequestException("TaxId is not informed ");
        }



        try {
            taxOut = taxService.findTaxById(taxId);

            if (taxOut == null) {
                throw new TaxNotFoundException("Tax ist not founded");
            }
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }

        return ResponseEntity.ok().body(taxOut);

    }

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
