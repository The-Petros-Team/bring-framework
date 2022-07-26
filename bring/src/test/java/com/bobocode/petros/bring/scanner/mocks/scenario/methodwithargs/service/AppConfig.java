package com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.service;

import com.bobocode.petros.bring.annotation.Bean;
import com.bobocode.petros.bring.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public PictureService pictureService(AnalyzerService analyzer) {
        return new NasaPictureService();
    }
}
