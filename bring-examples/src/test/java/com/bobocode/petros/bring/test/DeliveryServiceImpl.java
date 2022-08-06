package com.bobocode.petros.bring.test;

public class DeliveryServiceImpl implements DeliveryService {

    @Override
    public void deliver(String item) {
        System.out.println("Delivering " + item);
    }
}
