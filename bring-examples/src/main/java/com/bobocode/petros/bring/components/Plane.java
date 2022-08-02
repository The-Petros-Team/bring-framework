package com.bobocode.petros.bring.components;

import com.bobocode.petros.bring.annotation.Autowired;
import com.bobocode.petros.bring.annotation.Component;
import lombok.Getter;

@Getter
@Component
public class Plane implements Vehicle {

    @Autowired
    private Engine engine;

    @Override
    public Integer getSeatsNumber() {
        return 40;
    }
}
