package com.bobocode.petros.bring.context.mocks.registerbeandefinitions.service;

import com.bobocode.petros.bring.annotation.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public void printUserDetails(String userDetails) {
        System.out.println("User details: " + userDetails);
    }
}
