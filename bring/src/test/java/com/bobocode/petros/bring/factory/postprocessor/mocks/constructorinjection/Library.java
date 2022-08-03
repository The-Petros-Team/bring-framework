package com.bobocode.petros.bring.factory.postprocessor.mocks.constructorinjection;

import com.bobocode.petros.bring.annotation.Autowired;
import com.bobocode.petros.bring.annotation.Component;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Component
@NoArgsConstructor
public class Library {
    private Principal principal;

    private Location location;

    public Library(Principal principal) {
        this.principal = principal;
    }

    @Autowired
    public Library(Principal principal, Location location) {
        this.principal = principal;
        this.location = location;
    }
}
