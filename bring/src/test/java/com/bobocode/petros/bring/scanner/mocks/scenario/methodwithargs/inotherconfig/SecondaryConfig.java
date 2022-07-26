package com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.inotherconfig;

import com.bobocode.petros.bring.annotation.Bean;
import com.bobocode.petros.bring.annotation.Configuration;

@Configuration
public class SecondaryConfig {

    @Bean
    public Delivery delivery() {
        return new PizzaDelivery();
    }
}
