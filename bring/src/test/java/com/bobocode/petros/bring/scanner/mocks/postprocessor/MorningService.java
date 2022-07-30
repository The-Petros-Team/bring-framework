package com.bobocode.petros.bring.scanner.mocks.postprocessor;

import com.bobocode.petros.bring.annotation.Service;

@Service
public class MorningService implements GreetingService{
    @Override
    public void greeting() {
        System.out.println("Good morning!");
    }
}
