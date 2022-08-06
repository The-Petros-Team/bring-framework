package com.bobocode.petros.bring.factory.postprocessor.mocks.constructorinjection;

import com.bobocode.petros.bring.annotation.Component;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Component
@NoArgsConstructor
public class School {
    private Principal principal;

    private Location location;

    public School(Principal principal, Location location) {
        this.principal = principal;
        this.location = location;
    }
}
