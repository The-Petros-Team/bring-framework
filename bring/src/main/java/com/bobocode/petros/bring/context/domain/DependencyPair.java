package com.bobocode.petros.bring.context.domain;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class DependencyPair {
    private Object interfaceClass;
    private Object implementation;
}
