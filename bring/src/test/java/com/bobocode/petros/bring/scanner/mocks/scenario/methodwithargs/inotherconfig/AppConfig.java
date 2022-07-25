package com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.inotherconfig;

import com.bobocode.petros.bring.annotation.Bean;
import com.bobocode.petros.bring.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public PersonService personService(Delivery delivery) {
        return new ITPersonService();
    }
}
