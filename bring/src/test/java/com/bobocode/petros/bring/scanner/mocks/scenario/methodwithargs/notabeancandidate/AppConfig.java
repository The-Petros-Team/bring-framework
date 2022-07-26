package com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.notabeancandidate;

import com.bobocode.petros.bring.annotation.Bean;
import com.bobocode.petros.bring.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public RocketService rocketService(ExceptionHandler exceptionHandler) {
        return new UARocketService();
    }
}
