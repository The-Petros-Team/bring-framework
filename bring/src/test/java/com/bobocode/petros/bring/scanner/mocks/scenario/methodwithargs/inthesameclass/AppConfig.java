package com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.inthesameclass;

import com.bobocode.petros.bring.annotation.Bean;
import com.bobocode.petros.bring.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public TicketService ticketService(PaymentService paymentService) {
        return new BusTicketService();
    }

    @Bean
    public PaymentService paymentService() {
        return new PayPalPaymentService();
    }
}
