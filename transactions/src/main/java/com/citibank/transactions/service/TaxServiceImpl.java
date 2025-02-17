package com.citibank.transactions.service;

import com.citibank.transactions.domain.TaxIn;
import com.citibank.transactions.domain.TaxesData;
import com.citibank.transactions.repository.TaxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaxServiceImpl  implements TaxService{

    @Autowired
    TaxRepository taxRepository;

    @Override
    public void submitNewTax(TaxIn taxIn) {
        var taxData = new TaxesData();
        LocalDateTime auditDate = LocalDateTime.now();
        taxData.setTaxCat(taxIn.getTaxCat());
        taxData.setTaxValue(taxIn.getTaxValue());
        taxData.setInsertDate(auditDate.toString());
        taxData.setUpdateDate(auditDate.toString());
        taxRepository.save(taxData);
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
