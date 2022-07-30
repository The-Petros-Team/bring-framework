package com.bobocode.petros.bring.scanner.mocks.postprocessor;

public class EveningService implements GreetingService{
    @Override
    public void greeting() {
        System.out.println("Good evening!");
    }
}
