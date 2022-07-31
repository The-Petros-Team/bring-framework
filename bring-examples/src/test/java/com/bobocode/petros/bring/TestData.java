package com.bobocode.petros.bring;

import com.bobocode.petros.bring.context.ApplicationContext;
import com.bobocode.petros.bring.context.ApplicationContextContainer;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestData {

    public static final ApplicationContext CONTEXT = ApplicationContextContainer.create("com.bobocode.petros.bring");
}
