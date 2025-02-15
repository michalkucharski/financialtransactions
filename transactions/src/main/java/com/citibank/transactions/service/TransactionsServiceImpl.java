package com.citibank.transactions.service;

import com.citibank.transactions.domain.TransactionIn;
import com.citibank.transactions.domain.TransactionsData;
import com.citibank.transactions.repository.TaxRepository;
import com.citibank.transactions.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionsServiceImpl implements TransactionsService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TaxRepository taxRepository;

    @Override
    public TransactionsData submitTransaction(TransactionIn transaction) {

        return transactionRepository.save(transaction.getTransactionsData());
    }

    @Override
    public TransactionsData findTransactionyId(String transactionId) {
        return transactionRepository.findByTransactionId(transactionId).findFirst().orElse(null);
    }

    @Override
    public List<TransactionsData> findAllTransactions() {
        return transactionRepository.findAll();
    }
};
