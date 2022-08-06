package com.bobocode.petros.bring.context.mocks.registerbeandefinitions.service;

public class VisaPaymentService implements PaymentService {

    @Override
    public void payWithVisa(String cardDetails) {
        System.out.println("Paying with the following card details: " + cardDetails);
    }
}
