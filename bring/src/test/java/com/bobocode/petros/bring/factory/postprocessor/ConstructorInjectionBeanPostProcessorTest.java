package com.bobocode.petros.bring.factory.postprocessor;

import com.bobocode.petros.bring.context.ApplicationContext;
import com.bobocode.petros.bring.context.domain.BeanReference;
import com.bobocode.petros.bring.context.domain.BeanScope;
import com.bobocode.petros.bring.exception.NoSuchBeanException;
import com.bobocode.petros.bring.exception.NoUniqueAutowiredConstructorException;
import com.bobocode.petros.bring.factory.postprocessor.mocks.constructorinjection.Library;
import com.bobocode.petros.bring.factory.postprocessor.mocks.constructorinjection.Location;
import com.bobocode.petros.bring.factory.postprocessor.mocks.constructorinjection.Museum;
import com.bobocode.petros.bring.factory.postprocessor.mocks.constructorinjection.Principal;
import com.bobocode.petros.bring.factory.postprocessor.mocks.constructorinjection.School;
import com.bobocode.petros.bring.factory.postprocessor.mocks.constructorinjection.Theatre;
import com.bobocode.petros.bring.factory.postprocessor.mocks.constructorinjection.University;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConstructorInjectionBeanPostProcessorTest {
    @Mock
    private ApplicationContext applicationContext;

    private Principal principal = new Principal();
    private Location location = new Location();
    private Library library = new Library(null);

    @InjectMocks
    private final ConstructorInjectionBeanPostProcessor constructorInjectionBeanPostProcessor = new ConstructorInjectionBeanPostProcessor();

    @Test
    void whenOneAutowiredConstructorIsPresentAndBeansAreFoundByTypeThenInject() {
        when(applicationContext.getBean(Principal.class)).thenReturn(principal);
        when(applicationContext.getBean(Location.class)).thenReturn(location);
        BeanReference beanReference = new BeanReference(library, BeanScope.SINGLETON);

        constructorInjectionBeanPostProcessor.postProcessBeforeInitialization(beanReference);

        Library newBeanObject = (Library) beanReference.getBeanObject();

        assertEquals(principal, newBeanObject.getPrincipal());
        assertEquals(location, newBeanObject.getLocation());
    }

    @Test
    void whenOneConstructorIsPresentAndBeansAreNotFoundByTypeThenThrowException() {
        when(applicationContext.getBean(Principal.class)).thenThrow(NoSuchBeanException.class);
        BeanReference beanReference = new BeanReference(library, BeanScope.SINGLETON);

        assertThrows(NoSuchBeanException.class, () -> constructorInjectionBeanPostProcessor.postProcessBeforeInitialization(beanReference));
    }

    @Test
    void whenNoConstructorIsPresentThenDoNothing() {
        University university = new University();
        BeanReference beanReference = new BeanReference(university, BeanScope.SINGLETON);

        constructorInjectionBeanPostProcessor.postProcessBeforeInitialization(beanReference);

        University newBeanObject = (University) beanReference.getBeanObject();

        assertNull(newBeanObject.getPrincipal());
        assertNull(newBeanObject.getLocation());
    }

    @Test
    void whenMoreThanOneConstructorIsPresentThenDoNothing() {
        Theatre theatre = new Theatre(null, null);
        BeanReference beanReference = new BeanReference(theatre, BeanScope.SINGLETON);

        constructorInjectionBeanPostProcessor.postProcessBeforeInitialization(beanReference);

        Theatre newBeanObject = (Theatre) beanReference.getBeanObject();

        assertNull(newBeanObject.getPrincipal());
        assertNull(newBeanObject.getLocation());
    }

    @Test
    void whenMoreThanOneAutowiredConstructorIsPresentThenThrowException() {
        Museum museum = new Museum(null, null);
        BeanReference beanReference = new BeanReference(museum, BeanScope.SINGLETON);

        assertThrows(NoUniqueAutowiredConstructorException.class,
                () -> constructorInjectionBeanPostProcessor.postProcessBeforeInitialization(beanReference));
    }

}