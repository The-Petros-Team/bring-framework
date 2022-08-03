package com.bobocode.petros.bring.factory.postprocessor.mocks.constructorinjection;

import com.bobocode.petros.bring.annotation.Autowired;
import com.bobocode.petros.bring.annotation.Component;
import lombok.Getter;

@Getter
@Component
public class Museum {
    private Principal principal;

    private Location location;

    @Autowired
    public Museum(Principal principal) {
        this.principal = principal;
    }


    @Autowired
    public Museum(Principal principal, Location location) {
        this.principal = principal;
        this.location = location;
    }
}
