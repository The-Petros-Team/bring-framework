package com.bobocode.petros.bring.context.domain;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Dependency {
    private Object interfaceClass;
    private Object implementation;
}
