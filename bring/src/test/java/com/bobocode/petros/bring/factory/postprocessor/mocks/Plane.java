package com.bobocode.petros.bring.factory.postprocessor.mocks;

import com.bobocode.petros.bring.annotation.Component;
import lombok.Getter;

@Getter
@Component
public class Plane implements Vehicle {

    private Engine engine;
}
