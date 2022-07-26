package com.bobocode.petros.bring.registry;

import com.bobocode.petros.bring.context.domain.BeanDefinition;
import com.bobocode.petros.bring.exception.ExceptionMessage;
import com.bobocode.petros.bring.exception.InvalidBeanNameException;
import com.bobocode.petros.bring.exception.NoSuchBeanDefinitionException;
import com.bobocode.petros.bring.exception.NotUniqueBeanNameException;
import com.bobocode.petros.bring.registry.mocks.MorningService;
import com.bobocode.petros.bring.registry.utils.RegistryTestUtils;
import com.bobocode.petros.bring.utils.BeanTestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.bobocode.petros.bring.exception.ExceptionMessage.*;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
public class BeanDefinitionRegistryTest {

    @Spy
    private Map<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap<>();

    private BeanDefinitionRegistry registry;

    @BeforeEach
    public void setup() {
        openMocks(this);
        registry = DefaultBeanDefinitionRegistry.getInstance();
        RegistryTestUtils.configureRegistry(registry, beanDefinitions);
    }

    @AfterEach
    public void cleanup() {
        this.beanDefinitions.clear();
    }

    @Test
    public void whenRegisterBeanDefinitionThenRegisterItSuccessfully() {
        // given
        String beanName = "morningService";
        BeanDefinition beanDefinition = BeanTestUtils.createBeanDefinition(beanName, new MorningService());

        // then
        registry.registerBeanDefinition(beanName, beanDefinition);

        // assertions & verification
        verify(beanDefinitions, times(1)).put(beanName, beanDefinition);
    }

    @Test
    public void whenBeanNameIsNullThenThrowException() {
        // given
        String nullBeanName = null;
        BeanDefinition beanDefinition = BeanTestUtils.createBeanDefinition(nullBeanName, new MorningService());

        // assertions & verification
        final InvalidBeanNameException invalidBeanNameException = assertThrows(
                InvalidBeanNameException.class,
                () -> this.registry.registerBeanDefinition(nullBeanName, beanDefinition)
        );
        assertEquals(NULL_OR_EMPTY_BEAN_NAME, invalidBeanNameException.getMessage());
    }

    @Test
    public void whenBeanNameIsEmptyThenThrowException() {
        // given
        String emptyBeanName = "";
        BeanDefinition beanDefinition = BeanTestUtils.createBeanDefinition(emptyBeanName, new MorningService());

        // assertions & verification
        final InvalidBeanNameException invalidBeanNameException = assertThrows(
                InvalidBeanNameException.class,
                () -> this.registry.registerBeanDefinition(emptyBeanName, beanDefinition)
        );
        assertEquals(NULL_OR_EMPTY_BEAN_NAME, invalidBeanNameException.getMessage());
    }

    @Test
    public void whenBeanNameHasOnlyWhitespacesThenThrowException() {
        // given
        String whitespaceOnlyBeanName = "   ";
        BeanDefinition beanDefinition = BeanTestUtils.createBeanDefinition(whitespaceOnlyBeanName, new MorningService());

        // assertions & verification
        final InvalidBeanNameException invalidBeanNameException = assertThrows(
                InvalidBeanNameException.class,
                () -> this.registry.registerBeanDefinition(whitespaceOnlyBeanName, beanDefinition)
        );
        assertEquals(NULL_OR_EMPTY_BEAN_NAME, invalidBeanNameException.getMessage());
    }

    @Test
    public void whenRegisterBeanDefinitionWithTheSameNameThenThrowException() {
        // given
        String beanName = "morningService";
        BeanDefinition beanDefinition = BeanTestUtils.createBeanDefinition(beanName, new MorningService());

        // then
        this.registry.registerBeanDefinition(beanName, beanDefinition);

        assertEquals(1, this.beanDefinitions.size());
        assertTrue(this.beanDefinitions.containsKey(beanName));

        // assertions & verification
        final NotUniqueBeanNameException notUniqueBeanNameException = assertThrows(
                NotUniqueBeanNameException.class,
                () -> this.registry.registerBeanDefinition(beanName, beanDefinition)
        );
        assertEquals(format(BEAN_NAME_IS_NOT_UNIQUE, beanName), notUniqueBeanNameException.getMessage());
    }

    @Test
    public void whenRemoveBeanDefinitionThenItWasRemovedSuccessfully() {
        // given
        String beanName = "morningService";
        BeanDefinition beanDefinition = BeanTestUtils.createBeanDefinition(beanName, new MorningService());

        // when
        this.registry.registerBeanDefinition(beanName, beanDefinition);

        // then
        assertEquals(1, this.beanDefinitions.size());
        assertTrue(this.beanDefinitions.containsKey(beanName));
        this.registry.remove(beanName);

        // assertions & verification
        assertEquals(0, this.beanDefinitions.size());
        assertFalse(this.beanDefinitions.containsKey(beanName));
    }

    @Test
    public void whenRemoveBeanDefinitionAndNameIsNullThenThrowException() {
        // given
        String nullBeanName = null;

        // assertions & verification
        final InvalidBeanNameException invalidBeanNameException = assertThrows(
                InvalidBeanNameException.class,
                () -> this.registry.remove(nullBeanName)
        );
        assertEquals(NULL_OR_EMPTY_BEAN_NAME, invalidBeanNameException.getMessage());
    }

    @Test
    public void whenRemoveBeanDefinitionAndNameIsEmptyThenThrowException() {
        // given
        String emptyBeanName = "";

        // assertions & verification
        final InvalidBeanNameException invalidBeanNameException = assertThrows(
                InvalidBeanNameException.class,
                () -> this.registry.remove(emptyBeanName)
        );
        assertEquals(NULL_OR_EMPTY_BEAN_NAME, invalidBeanNameException.getMessage());
    }

    @Test
    public void whenRemoveBeanDefinitionAndNameContainsOnlyWhitespacesThenThrowException() {
        // given
        String blankBeanName = "   ";

        // assertions & verification
        final InvalidBeanNameException invalidBeanNameException = assertThrows(
                InvalidBeanNameException.class,
                () -> this.registry.remove(blankBeanName)
        );
        assertEquals(NULL_OR_EMPTY_BEAN_NAME, invalidBeanNameException.getMessage());
    }

    @Test
    public void whenRemoveNotRegisteredBeanDefinitionThenThrowException() {
        // given
        String beanName = "notRegisteredBeanName";

        // assertions & verification
        final NoSuchBeanDefinitionException noSuchBeanException = assertThrows(
                NoSuchBeanDefinitionException.class,
                () -> this.registry.remove(beanName)
        );
        assertEquals(format(ExceptionMessage.NO_SUCH_BEAN_DEFINITION, beanName), noSuchBeanException.getMessage());
    }

    @Test
    public void whenGetBeanDefinitionThenReturnFoundBeanDefinitionSuccessfully() {
        // given
        String beanName = "morningService";
        BeanDefinition beanDefinition = BeanTestUtils.createBeanDefinition(beanName, new MorningService());

        // when
        this.registry.registerBeanDefinition(beanName, beanDefinition);

        // then
        assertEquals(1, this.beanDefinitions.size());
        assertTrue(this.beanDefinitions.containsKey(beanName));
        final BeanDefinition foundBeanDefinition = this.registry.getBeanDefinition(beanName);

        // assertions & verification
        assertNotNull(foundBeanDefinition);
        assertEquals(beanDefinition, foundBeanDefinition);
    }

    @Test
    public void whenGetBeanDefinitionByNullBeanNameThenThrowException() {
        // given
        String nullBeanName = null;

        // assertions & verification
        final InvalidBeanNameException invalidBeanNameException = assertThrows(
                InvalidBeanNameException.class,
                () -> this.registry.getBeanDefinition(nullBeanName)
        );
        assertEquals(NULL_OR_EMPTY_BEAN_NAME, invalidBeanNameException.getMessage());
    }

    @Test
    public void whenGetBeanDefinitionByEmptyBeanNameThenThrowException() {
        // given
        String emptyBeanName = "";

        // assertions & verification
        final InvalidBeanNameException invalidBeanNameException = assertThrows(
                InvalidBeanNameException.class,
                () -> this.registry.getBeanDefinition(emptyBeanName)
        );
        assertEquals(NULL_OR_EMPTY_BEAN_NAME, invalidBeanNameException.getMessage());
    }

    @Test
    public void whenGetBeanDefinitionByBlankBeanNameThenThrowException() {
        // given
        String blankBeanName = "   ";

        // assertions & verification
        final InvalidBeanNameException invalidBeanNameException = assertThrows(
                InvalidBeanNameException.class,
                () -> this.registry.getBeanDefinition(blankBeanName)
        );
        assertEquals(NULL_OR_EMPTY_BEAN_NAME, invalidBeanNameException.getMessage());
    }

    @Test
    public void whenGetNotRegisteredBeanDefinitionThenThrowException() {
        // given
        String notRegisteredBeanName = "notRegisteredBeanName";

        // assertions & verification
        final NoSuchBeanDefinitionException noSuchBeanException = assertThrows(
                NoSuchBeanDefinitionException.class,
                () -> this.registry.getBeanDefinition(notRegisteredBeanName)
        );
        assertEquals(format(NO_SUCH_BEAN_DEFINITION, notRegisteredBeanName), noSuchBeanException.getMessage());
    }

    @Test
    public void whenRegistryContainsBeanDefinitionThenReturnTrue() {
        // given
        String beanName = "morningService";
        BeanDefinition beanDefinition = BeanTestUtils.createBeanDefinition(beanName, new MorningService());

        // when
        this.registry.registerBeanDefinition(beanName, beanDefinition);

        // then
        assertEquals(1, this.beanDefinitions.size());
        assertTrue(this.registry.containsBeanDefinition(beanName));
    }

    @Test
    public void whenRegistryDoesNotContainBeanDefinitionThenReturnFalse() {
        // given
        String beanName = "morningService";

        // then
        assertFalse(this.registry.containsBeanDefinition(beanName));
    }

    @Test
    public void whenRegistryDoesNotContainBeanDefinitionWithNullBeanNameThenThrowException() {
        // given
        String nullBeanName = null;

        // assertions & verification
        final InvalidBeanNameException invalidBeanNameException = assertThrows(
                InvalidBeanNameException.class,
                () -> this.registry.containsBeanDefinition(nullBeanName)
        );
        assertEquals(NULL_OR_EMPTY_BEAN_NAME, invalidBeanNameException.getMessage());
    }

    @Test
    public void whenRegistryDoesNotContainBeanDefinitionWithEmptyBeanNameThenThrowException() {
        // given
        String emptyBeanName = "";

        // assertions & verification
        final InvalidBeanNameException invalidBeanNameException = assertThrows(
                InvalidBeanNameException.class,
                () -> this.registry.containsBeanDefinition(emptyBeanName)
        );
        assertEquals(NULL_OR_EMPTY_BEAN_NAME, invalidBeanNameException.getMessage());
    }

    @Test
    public void whenRegistryDoesNotContainBeanDefinitionWithBlankBeanNameThenThrowException() {
        // given
        String blankBeanName = "   ";

        // assertions & verification
        final InvalidBeanNameException invalidBeanNameException = assertThrows(
                InvalidBeanNameException.class,
                () -> this.registry.containsBeanDefinition(blankBeanName)
        );
        assertEquals(NULL_OR_EMPTY_BEAN_NAME, invalidBeanNameException.getMessage());
    }

    @Test
    public void whenGetAllBeanDefinitionFromEmptyRegistryThenReceiveAnEmptyCollection() {
        //when
        var definitionCollection = this.registry.getAllBeanDefinitions();

        //then
        assertTrue(definitionCollection.isEmpty());

        //assertions & verification
        assertNotNull(definitionCollection);
    }

    @Test
    public void whenGetAllBeanDefinitionFromRegistryThenReceiveADefinitionCollection() {
        //given
        String beanName = "morningService";
        BeanDefinition beanDefinition = BeanTestUtils.createBeanDefinition(beanName, new MorningService());

        //when
        this.registry.registerBeanDefinition(beanName, beanDefinition);
        var definitionCollection = this.registry.getAllBeanDefinitions();

        //then
        assertFalse(definitionCollection.isEmpty());
        assertEquals(1, definitionCollection.size());
        assertEquals(beanDefinition, definitionCollection.toArray()[0]);
        assertTrue(definitionCollection.remove(beanDefinition));
        assertFalse(definitionCollection.contains(beanDefinition));

        //assertions & verification
        assertNotNull(definitionCollection);
        assertNotNull(beanDefinition);
    }
}
