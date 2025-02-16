package com.citibank.transactions.service;

import com.citibank.transactions.domain.TaxIn;
import com.citibank.transactions.domain.TaxesData;
import com.citibank.transactions.repository.TaxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaxServiceImpl  implements TaxService{

    @Autowired
    TaxRepository taxRepository;

    @Override
    public TaxesData submitNewTax(TaxIn taxIn) {
        return taxRepository.save(taxIn.getTaxesData());
    }

    @Override
    public TaxesData findTaxById(String taxId) {
        return taxRepository.findByTaxId(taxId).findFirst().orElse(null);
    }

    @Override
    public List<TaxesData> findAllTaxes() {
        return taxRepository.findAll();
    }
}
