package com.mybank.transactions.service;

import com.mybank.transactions.domain.TransactionIn;
import com.mybank.transactions.domain.TransactionsData;
import com.mybank.transactions.exceptions.TaxNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionsService{

    public void submitTransaction(TransactionIn transaction) throws TaxNotFoundException;
    public TransactionsData findTransactionyId(String transacctionId);
    public List<TransactionsData> findAllTransactions();
}
