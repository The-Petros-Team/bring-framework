package com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.inotherconfig;

public class ITPersonService implements PersonService {

    @Override
    public void notifyPerson(String fullName) {
        System.out.printf("Dear %s, your delivery is waiting for you!%n", fullName);
    }
}
