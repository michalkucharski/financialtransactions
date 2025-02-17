package com.citibank.transactions.service;

import com.citibank.transactions.domain.TaxIn;
import com.citibank.transactions.domain.TaxesData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaxService {

    public void submitNewTax(TaxIn taxIn);
    public TaxesData findTaxById(String taxId);
    public List<TaxesData> findAllTaxes();
}
