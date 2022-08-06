package com.bobocode.petros.bring.test;

import com.bobocode.petros.bring.annotation.Autowired;
import com.bobocode.petros.bring.annotation.Component;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private DeliveryService deliveryService;

    @Override
    public void printUsername(String username) {
        deliveryService.deliver("pizza");
        System.out.println("username: " + username);
    }
}
