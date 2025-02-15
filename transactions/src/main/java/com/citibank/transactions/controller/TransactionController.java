package com.citibank.transactions.controller;

import com.citibank.transactions.domain.APIResponse;
import com.citibank.transactions.domain.TransactionIn;
import com.citibank.transactions.service.TransactionsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/transactions/v1")
public class TransactionController {

    @Autowired
    TransactionsServiceImpl transactionsService;

    private static final APIResponse RESPONSE = new APIResponse();

    @PostMapping(path="/submitTransaction")
    public ResponseEntity<?> submitTransaction(@RequestBody TransactionIn transactionIn) {

        if (transactionIn.getTransactionsData() == null) {
            RESPONSE.setCode(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(cuenta, HttpStatus.OK);
        }
        CuentaInversion cuenta = cuentaService.findByIdCuenta(id);

        if (cuenta != null) {
            return new ResponseEntity<>(cuenta, HttpStatus.OK);
        } else {
            final String token = getToken();
            System.out.println("Token:" + token);
            CuentaInversion response = iCuentaRes.CUENTA_INVERSION(token, id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

    }

    @GetMapping(path="/consultaCuenta/{id}")
    public ResponseEntity<?> getCuenta(@PathVariable("id") String id){

        CuentaInversion cuenta = cuentaService.findByIdCuenta(id);

        if (cuenta != null) {
            return new ResponseEntity<>(cuenta, HttpStatus.OK);
        } else {
            final String token = getToken();
            System.out.println("Token:" + token);
            CuentaInversion response = iCuentaRes.CUENTA_INVERSION(token, id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

    }
}
