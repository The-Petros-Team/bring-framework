package com.bobocode.petros.bring.test;

import com.bobocode.petros.bring.annotation.Bean;
import com.bobocode.petros.bring.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public DeliveryService deliveryService() {
        return new DeliveryServiceImpl();
    }
}
