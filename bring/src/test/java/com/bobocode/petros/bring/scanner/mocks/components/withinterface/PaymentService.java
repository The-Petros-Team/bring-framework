package com.bobocode.petros.bring.scanner.mocks.components.withinterface;

import com.bobocode.petros.bring.annotation.Service;

@Service
public class PaymentService {

    public void pay(double amount) {
        System.out.println("Your payment: " + amount);
    }
}
