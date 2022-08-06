package com.bobocode.petros.bring.components;

import com.bobocode.petros.bring.annotation.Autowired;
import com.bobocode.petros.bring.annotation.Component;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Component
@NoArgsConstructor
public class Helicopter implements Vehicle {
    private Engine engine;

    @Autowired
    public Helicopter(Engine engine) {
        this.engine = engine;
    }

    @Override
    public Integer getSeatsNumber() {
        return 2;
    }
}
