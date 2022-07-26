package com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.inotherconfig;

public class PizzaDelivery implements Delivery {

    @Override
    public void deliver(String dish) {
        System.out.printf("Delivering pizza '%s'%n", dish);
    }
}
