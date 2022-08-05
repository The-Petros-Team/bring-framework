package com.bobocode.petros.bring.scanner.mocks.components.withinterface;

import com.bobocode.petros.bring.annotation.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public void makeOrder(String item) {
        System.out.println("I would like to order " + item);
    }
}
