package com.bobocode.petros.bring.factory.postprocessor.mocks.constructorinjection;

import com.bobocode.petros.bring.annotation.Autowired;
import com.bobocode.petros.bring.annotation.Component;
import lombok.Getter;

@Getter
@Component
public class Library {
    private Principal principal;

    private Location location;

    @Autowired
    public Library(Principal principal) {
        this.principal = principal;
    }

    public Library(Principal principal, Location location) {
        this.principal = principal;
        this.location = location;
    }
}
