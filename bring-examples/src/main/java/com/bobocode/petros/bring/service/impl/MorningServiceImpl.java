package com.bobocode.petros.bring.service.impl;

import com.bobocode.petros.bring.annotation.Autowired;
import com.bobocode.petros.bring.annotation.Service;
import com.bobocode.petros.bring.service.GreetingService;
import lombok.Getter;

@Service
public class MorningServiceImpl implements GreetingService {

    @Getter
    @Autowired
    private EveningServiceImpl eveningService;

    @Override
    public String createGreeting(String name) {
        return "Good morning, %s!".formatted(name);
    }
}
