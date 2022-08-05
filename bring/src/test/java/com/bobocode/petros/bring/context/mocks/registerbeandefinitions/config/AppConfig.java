package com.bobocode.petros.bring.context.mocks.registerbeandefinitions.config;

import com.bobocode.petros.bring.annotation.Bean;
import com.bobocode.petros.bring.annotation.Configuration;
import com.bobocode.petros.bring.context.mocks.registerbeandefinitions.service.PaymentService;
import com.bobocode.petros.bring.context.mocks.registerbeandefinitions.service.VisaPaymentService;

@Configuration
public class AppConfig {

    @Bean
    public PaymentService paymentService() {
        return new VisaPaymentService();
    }
}
