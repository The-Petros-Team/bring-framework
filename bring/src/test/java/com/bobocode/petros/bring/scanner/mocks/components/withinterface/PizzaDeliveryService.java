package com.bobocode.petros.bring.scanner.mocks.components.withinterface;

import com.bobocode.petros.bring.annotation.Autowired;
import com.bobocode.petros.bring.annotation.Service;

@Service
public class PizzaDeliveryService implements DeliveryService {

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;

    @Override
    public void deliver(String item) {
        userService.makeOrder(item);
        System.out.println(item + " is delivered. Enjoy!");
    }
}
