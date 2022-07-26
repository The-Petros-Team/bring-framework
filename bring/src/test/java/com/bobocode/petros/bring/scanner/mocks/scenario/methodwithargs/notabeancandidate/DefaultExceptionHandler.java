package com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.notabeancandidate;

public class DefaultExceptionHandler implements ExceptionHandler {

    @Override
    public void handle(String exception) {
        System.out.printf("Handling exception of type: '%s'%n", exception);
    }
}
