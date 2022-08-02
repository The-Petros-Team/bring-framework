package com.bobocode.petros.bring.context.aware.service;

import com.bobocode.petros.bring.context.aware.ApplicationContextAware;

public class EveningService implements ApplicationContextAware {

    public void greeting() {
        System.out.println("Good evening!");
    }
}
