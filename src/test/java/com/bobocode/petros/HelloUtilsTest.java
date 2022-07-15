package com.bobocode.petros;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HelloUtilsTest {

    private static HelloUtils helloUtils;

    @BeforeAll
    public static void setup() {
        helloUtils = new HelloUtils();
    }

    @Test
    public void sayHelloTest() {
        String name = "Vlad";
        final String message = helloUtils.sayHello(name);

        assertNotNull(message);
        assertEquals("Hello Vlad", message);
    }
}
