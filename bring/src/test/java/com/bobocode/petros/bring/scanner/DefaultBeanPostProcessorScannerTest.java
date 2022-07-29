package com.bobocode.petros.bring.scanner;

import com.bobocode.petros.bring.factory.postprocessor.BeanPostProcessor;
import com.bobocode.petros.bring.scanner.impl.DefaultBeanPostProcessorScanner;
import com.bobocode.petros.bring.scanner.mocks.EveningBeanPostProcessor;
import com.bobocode.petros.bring.scanner.mocks.EveningService;
import com.bobocode.petros.bring.scanner.mocks.MorningBeanPostProcessor;
import com.bobocode.petros.bring.scanner.mocks.MorningService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith({MockitoExtension.class})
class DefaultBeanPostProcessorScannerTest {
    private final Set<Class<?>> classes = new HashSet<>();
    private DefaultBeanPostProcessorScanner scanner;

    @BeforeEach
    void setup() {
        openMocks(this);
        classes.addAll(List.of(
                MorningService.class,
                EveningService.class,
                MorningBeanPostProcessor.class,
                EveningBeanPostProcessor.class
        ));
        scanner = new DefaultBeanPostProcessorScanner();
    }

    @AfterEach
    void clear() {
        classes.clear();
    }

    @Test
    void whenScanSetOfClassesThatContainsBeanPostProcessorsThenReturnListOfBeanPostProcessors() {
        //then
        var postProcessorsList = scanner.scan(classes);

        //assertion & error
        assertAll(
                () -> assertNotNull(postProcessorsList),
                () -> assertEquals(2, postProcessorsList.size()),
                () -> assertTrue(postProcessorsList.stream().allMatch(processor -> BeanPostProcessor.class.isAssignableFrom(processor.getClass())))
        );
    }

    @Test
    void whenScanSetOfClassesThatNotContainsBeanPostProcessorsThenReturnEmptyList() {
        //given
        var classesSet = new HashSet<Class<?>>() {{
            add(null);
            add(MorningService.class);
        }};

        //then
        var postProcessorsList = scanner.scan(classesSet);

        //assertion & error
        assertAll(
                () -> assertNotNull(postProcessorsList),
                () -> assertEquals(0, postProcessorsList.size()),
                () -> assertTrue(postProcessorsList.isEmpty())
        );
    }

    @Test
    void whenSetForScanningContainsNullThenAllWorkCorrect() {
        //given
        var classSet = new HashSet<Class<?>>() {{
            add(null);
            add(MorningBeanPostProcessor.class);
        }};

        //then
        var postProcessorsList = scanner.scan(classSet);

        //assertion & error
        assertAll(
                () -> assertNotNull(classSet),
                () -> assertNotNull(postProcessorsList),
                () -> assertTrue(postProcessorsList.stream()
                        .allMatch(processor -> BeanPostProcessor.class.isAssignableFrom(processor.getClass()))),
                () -> assertTrue(postProcessorsList.stream()
                        .allMatch(processor -> processor.getClass().isAssignableFrom(MorningBeanPostProcessor.class)))
        );
    }
}
