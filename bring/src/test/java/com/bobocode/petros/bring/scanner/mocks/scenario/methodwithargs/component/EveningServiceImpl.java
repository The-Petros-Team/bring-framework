package com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.component;

import com.bobocode.petros.bring.annotation.Component;

@Component
public class EveningServiceImpl implements EveningService {

    @Override
    public void wishGoodNight(String name) {
        System.out.printf("Good night, %s%n", name);
    }
}
