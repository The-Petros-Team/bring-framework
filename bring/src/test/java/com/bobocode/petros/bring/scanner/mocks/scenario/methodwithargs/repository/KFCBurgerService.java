package com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.repository;

public class KFCBurgerService implements BurgerService {

    @Override
    public void makeBurger(String burgerType) {
        System.out.printf("%s burger is prepared. Bone appetit!", burgerType);
    }
}
