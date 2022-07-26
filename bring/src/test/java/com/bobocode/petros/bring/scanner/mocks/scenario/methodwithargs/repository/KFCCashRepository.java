package com.bobocode.petros.bring.scanner.mocks.scenario.methodwithargs.repository;

import com.bobocode.petros.bring.annotation.Repository;

@Repository
public class KFCCashRepository implements CashRepository {

    @Override
    public void pay(String dish) {
        System.out.printf("You've successfully paid for KFC %s", dish);
    }
}
