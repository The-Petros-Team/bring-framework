package com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.inthesameclass;

public class BusTicketService implements TicketService {

    @Override
    public void bookTicket(String number) {
        System.out.printf("Booking a ticket %s", number);
    }
}
