package com.citibank.transactions.service;

import com.citibank.transactions.domain.TransactionIn;
import com.citibank.transactions.domain.TransactionsData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionsService{

    public TransactionsData submitTransaction(TransactionIn transaction) ;
    public TransactionsData findTransactionyId(String transacctionId);
    public List<TransactionsData> findAllTransactions();
}
