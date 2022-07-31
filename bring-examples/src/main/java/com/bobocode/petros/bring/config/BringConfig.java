package com.bobocode.petros.bring.config;

import com.bobocode.petros.bring.annotation.Bean;
import com.bobocode.petros.bring.annotation.Configuration;
import com.bobocode.petros.bring.components.Engine;

@Configuration
public class BringConfig {

    @Bean
    public Engine engine() {
        return new Engine();
    }

}
