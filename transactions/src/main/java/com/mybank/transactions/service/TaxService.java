package com.mybank.transactions.service;

import com.mybank.transactions.domain.TaxIn;
import com.mybank.transactions.domain.TaxesData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaxService {

    public TaxesData submitNewTax(TaxIn taxIn);
    public TaxesData findTaxById(String taxId);
    public List<TaxesData> findAllTaxes();
}
