package com.bobocode.petros.bring.test;

import com.bobocode.petros.bring.context.ApplicationContextContainer;

public class AppRunner {
    public static void main(String[] args) {
        var context = ApplicationContextContainer.create("com.bobocode.petros.bring");
        final UserService userService = context.getBean(UserService.class);
        userService.printUsername("Vlad");
    }
}
