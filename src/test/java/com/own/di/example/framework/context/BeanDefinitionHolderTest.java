package com.own.di.example.framework.context;

import com.own.di.example.test.di.implementation.formatters.BankAccountNumberFormatter;
import com.own.di.example.test.di.implementation.formatters.CurrencyFormatter;
import com.own.di.example.test.di.implementation.formatters.DateFormatter;
import com.own.di.example.test.di.interfaces.Formatter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.junit.jupiter.api.Test;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BeanDefinitionHolderTest {
    private final BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder();
    private static final String BANK_ACCOUNT_NUMBER_FORMATTER = "bankAccountNumberFormatter";
    private static final String CURRENCY_FORMATTER = "currencyFormatter";
    private static final String DATE_FORMATTER = "dateFormatter";

    {
        beanDefinitionHolder.add(createBeanDefinition(BankAccountNumberFormatter.class,
                new BankAccountNumberFormatter(), BANK_ACCOUNT_NUMBER_FORMATTER));
        beanDefinitionHolder.add(createBeanDefinition(CurrencyFormatter.class,
                new CurrencyFormatter(), CURRENCY_FORMATTER));
        beanDefinitionHolder.add(createBeanDefinition(DateFormatter.class,
                new DateFormatter(), DATE_FORMATTER));
    }

    @ParameterizedTest
    @MethodSource("provideClasses")
    public void testContainsKey(Class<Formatter> clazz) {
        assertTrue(beanDefinitionHolder.containsKey(clazz));
    }

    @ParameterizedTest
    @MethodSource("provideClassesWithQualifiers")
    public void testContainsKey(Class<Formatter> clazz, String qualifier) {
        assertTrue(beanDefinitionHolder.containsKey(clazz, qualifier));
    }

    @ParameterizedTest
    @MethodSource("provideClasses")
    public void testGetBean(Class<Formatter> clazz) {
        assertNotNull(beanDefinitionHolder.get(clazz));
    }

    @ParameterizedTest
    @MethodSource("provideClassesWithQualifiers")
    public void testGetBean(Class<Formatter> clazz, String qualifier) {
        assertNotNull(beanDefinitionHolder.get(clazz, qualifier));
    }

    @Test
    public void shouldAddAndGetBeanByType() {
        BeanDefinitionHolder<TestService> holder = new BeanDefinitionHolder<>();
        TestService service = new TestService();

        holder.add(BeanDefinition.builder()
                .clazz(TestService.class)
                .object(service)
                .build());

        assertTrue(holder.containsKey(TestService.class));
        assertSame(service, holder.get(TestService.class));
    }

    @Test
    public void shouldAddAndGetBeanByTypeAndQualifier() {
        BeanDefinitionHolder<TestService> holder = new BeanDefinitionHolder<>();
        TestService service = new TestService();

        holder.add(BeanDefinition.builder()
                .clazz(TestService.class)
                .object(service)
                .alias("primary")
                .build());

        assertTrue(holder.containsKey(TestService.class, "primary"));
        assertSame(service, holder.get(TestService.class, "primary"));
    }

    @Test
    public void shouldReturnFalseWhenBeanDoesNotExist() {
        BeanDefinitionHolder<TestService> holder = new BeanDefinitionHolder<>();

        assertFalse(holder.containsKey(TestService.class));
    }

    @Test
    public void shouldThrowExceptionWhenBeanDoesNotExist() {
        BeanDefinitionHolder<TestService> holder = new BeanDefinitionHolder<>();

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> holder.get(TestService.class)
        );

        assertTrue(exception.getMessage().contains("There is not object for following type"));
    }

    private static class TestService {
    }


    private static Stream<Arguments> provideClasses() {
        return Stream.of(
                Arguments.of(BankAccountNumberFormatter.class),
                Arguments.of(CurrencyFormatter.class),
                Arguments.of(DateFormatter.class)
        );
    }

    private static Stream<Arguments> provideClassesWithQualifiers() {
        return Stream.of(
                Arguments.of(BankAccountNumberFormatter.class, BANK_ACCOUNT_NUMBER_FORMATTER),
                Arguments.of(CurrencyFormatter.class, CURRENCY_FORMATTER),
                Arguments.of(DateFormatter.class, DATE_FORMATTER)
        );
    }

    private static BeanDefinition createBeanDefinition(Class clazz, Object object, String alias) {
        return BeanDefinition.builder()
                .clazz(clazz)
                .object(object)
                .alias(alias)
                .build();
    }
}