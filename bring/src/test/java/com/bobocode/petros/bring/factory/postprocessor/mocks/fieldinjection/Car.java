package com.bobocode.petros.bring.factory.postprocessor.mocks.fieldinjection;

import com.bobocode.petros.bring.annotation.Autowired;
import com.bobocode.petros.bring.annotation.Component;
import lombok.Getter;

@Getter
@Component
public class Car implements Vehicle {

    @Autowired
    private Engine engine;
}
