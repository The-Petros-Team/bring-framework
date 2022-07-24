package com.bobocode.petros.bring.config;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

// TODO: 24.07.2022
@Getter
@Setter
public class ContextConfig {
    private Set<String> packagesToScan = new HashSet<>();
    private Set<Class<?>> registerClasses = new HashSet<>();

    public void addPackages(String... packagesToScan) {
        this.packagesToScan.addAll(Arrays.asList(packagesToScan));
    }

    public void registerClass(Class<?> clazz) {
        registerClasses.add(clazz);
    }
}
