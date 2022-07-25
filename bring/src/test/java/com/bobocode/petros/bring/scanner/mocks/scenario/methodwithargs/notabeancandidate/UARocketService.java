package com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.notabeancandidate;

public class UARocketService implements RocketService {

    @Override
    public void launch(String rocketType) {
        System.out.printf("Launching UA rocket of type '%s' to the space...%n", rocketType);
    }
}
