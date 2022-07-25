package com.bobocode.petros.bring.scanner.mocks.scenario.privatemethod;

public class DefaultMessageService implements MessageService {

    @Override
    public void sendMessage(String message) {
        System.out.printf("Sending message: %s%n", message);
    }
}
