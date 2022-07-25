package com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.repository;

import com.bobocode.petros.bring.annotation.Bean;
import com.bobocode.petros.bring.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public BurgerService burgerService(CashRepository cashRepository) {
        return new KFCBurgerService();
    }
}
