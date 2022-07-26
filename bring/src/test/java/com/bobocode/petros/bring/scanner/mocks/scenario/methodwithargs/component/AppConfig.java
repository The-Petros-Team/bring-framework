package com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.component;

import com.bobocode.petros.bring.annotation.Bean;
import com.bobocode.petros.bring.annotation.Configuration;

@Configuration
public class AppConfig {
    
    @Bean
    public CloudService cloudService(EveningService eveningService) {
        return new GoogleCloudService();
    }
}
