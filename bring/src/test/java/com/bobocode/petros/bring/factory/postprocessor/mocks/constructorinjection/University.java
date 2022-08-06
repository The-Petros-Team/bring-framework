package com.bobocode.petros.bring.factory.postprocessor.mocks.constructorinjection;

import com.bobocode.petros.bring.annotation.Component;
import lombok.Getter;

@Getter
@Component
public class University {

    private Principal principal;

    private Location location;

}
