package com.citibank.transactions.service;

import com.citibank.transactions.domain.TaxesData;
import com.citibank.transactions.domain.TransactionIn;
import com.citibank.transactions.domain.TransactionsData;
import com.citibank.transactions.enums.TaxCategory;
import com.citibank.transactions.exceptions.TaxNotFoundException;
import com.citibank.transactions.repository.TaxRepository;
import com.citibank.transactions.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionsServiceImpl implements TransactionsService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TaxRepository taxRepository;

    @Override
    public void submitTransaction(TransactionIn transaction) throws TaxNotFoundException{

        var trxData = new TransactionsData();
        LocalDateTime auditDate = LocalDateTime.now();
        trxData.setTransactionId(transaction.getTransactionId());
        trxData.setGoodsType(transaction.getGoodsType());
        trxData.setGrossAmount(transaction.getAmount());
        trxData.setInsertDate(auditDate.toString());
        trxData.setUpdateDate(auditDate.toString());
        String taxCat = String.valueOf(TaxCategory.valueOf(trxData.getGoodsType()));
        TaxesData taxData = taxRepository.findByTaxId(taxCat).findFirst().orElseThrow(() -> new TaxNotFoundException("The tax value was not founded"));
        trxData.setNetAmount(transaction.getAmount() - transaction.getAmount()*taxData.getTaxValue());
        transactionRepository.save(trxData);
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
