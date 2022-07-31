package com.bobocode.petros.bring.service.impl;

import com.bobocode.petros.bring.annotation.Autowired;
import com.bobocode.petros.bring.annotation.Service;
import com.bobocode.petros.bring.service.GreetingService;
import lombok.Getter;

@Service(name = "evening")
public class EveningServiceImpl  implements GreetingService {

    @Getter
    @Autowired
    private MorningServiceImpl morningService;

    @Override
    public String createGreeting(String name) {
        return "Good evening, %s!".formatted(name);
    }
}
