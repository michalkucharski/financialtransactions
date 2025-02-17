package com.citibank.transactions.service;

import com.citibank.transactions.domain.TransactionIn;
import com.citibank.transactions.domain.TransactionsData;
import com.citibank.transactions.exceptions.TaxNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionsService{

    public void submitTransaction(TransactionIn transaction) throws TaxNotFoundException;
    public TransactionsData findTransactionyId(String transacctionId);
    public List<TransactionsData> findAllTransactions();
}
