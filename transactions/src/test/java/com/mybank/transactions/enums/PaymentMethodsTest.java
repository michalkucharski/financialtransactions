package com.mybank.transactions.enums;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PaymentMethodsTest {

    private final static int Payment_METHODS = 3;
    @Test
    void checkPaymentMethodsDataIntegrity() {
        assertEquals(Payment_METHODS, PaymentMethods.values().length);

        for (PaymentMethods payments : PaymentMethods.values()) {
            assertTrue("Value for payment method is informed ",
                    !payments.getPaymentMethod().isEmpty());
        }
    }


}
