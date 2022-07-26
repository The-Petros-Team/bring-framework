package com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.inthesameclass;

public class PayPalPaymentService implements PaymentService {

    @Override
    public void pay(String paymentType) {
        System.out.printf("Paying with %s", paymentType);
    }
}
