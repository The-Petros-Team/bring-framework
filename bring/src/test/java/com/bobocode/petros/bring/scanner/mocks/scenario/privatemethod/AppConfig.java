package com.bobocode.petros.bring.scanner.mocks.scenario.privatemethod;

import com.bobocode.petros.bring.annotation.Bean;
import com.bobocode.petros.bring.annotation.Configuration;

@Configuration
public class AppConfig {
    
    @Bean
    public CloudService cloudService() {
        return new GoogleCloudService();
    }
    
    @Bean
    private MessageService messageService() {
        return new DefaultMessageService();
    }
}
