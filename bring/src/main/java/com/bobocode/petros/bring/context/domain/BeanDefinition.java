package com.bobocode.petros.bring.context.domain;

import lombok.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class BeanDefinition {
    private String beanName;
    private String scope;
    private Object beanClass;
    private boolean requiresAutowire;
    private boolean isInterface;
    private Map<String, Object> implementations = new ConcurrentHashMap<>();
    private Map<String, Dependency> dependencies = new ConcurrentHashMap<>();
}
