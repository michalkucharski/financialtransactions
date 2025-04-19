package com.mybank.transactions.controller;

import com.mybank.transactions.domain.TaxIn;
import com.mybank.transactions.domain.TaxesData;
import com.mybank.transactions.domain.TransactionIn;
import com.mybank.transactions.domain.TransactionsData;
import com.mybank.transactions.enums.PaymentMethods;
import com.mybank.transactions.exceptions.InvalidRequestException;
import com.mybank.transactions.exceptions.TaxNotFoundException;
import com.mybank.transactions.exceptions.TransactionNotFoundException;
import com.mybank.transactions.service.TaxServiceImpl;
import com.mybank.transactions.service.TransactionsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/transactions/v1")
public class TransactionController {

    @Autowired
    TransactionsServiceImpl transactionsService;

    @Autowired
    TaxServiceImpl taxService;

    @Operation(summary = "Submit new transaction")
    @PostMapping(path="/submitTransaction")
    public ResponseEntity<?> submitTransaction(@RequestBody TransactionIn transactionIn) throws InvalidRequestException {

        if (transactionIn == null) {
            throw new InvalidRequestException("No data to submit");
        }

        if (transactionIn.transactionId().isEmpty()) {
            throw new InvalidRequestException("Transaction Id is not informed");
        }

        if (transactionIn.amount() < 1 ) {
            throw new InvalidRequestException("Transaction amount should be more than 0");
        }

        if (transactionIn.paymentMethod().isEmpty()) {
            throw new InvalidRequestException("Payment method should be informed");
        }

        if(transactionIn.goodsType().isEmpty()) {
            throw new InvalidRequestException("Type of goods should be informed");
        }

        if(transactionIn.taxCat().isEmpty()) {
            throw new InvalidRequestException("Tax category should be informed");
        }

        String paymentMetUp = transactionIn.paymentMethod().toUpperCase();
        if (PaymentMethods.valueOf(paymentMetUp) == null) {
            throw new InvalidRequestException("Payment methods is not allowed");
        }
        try {
            transactionsService.submitTransaction(transactionIn);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }

        return ResponseEntity.accepted().body("The new transaction was submitted successfully");
    }

    @Operation(summary = "Get a transaction by its id")
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

    @Operation(summary = "Retrieve all transactions")
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


}
