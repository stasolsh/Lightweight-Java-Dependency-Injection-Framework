package com.own.di.example.framework.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UtilsTest {

    @Test
    public void shouldReturnTrueWhenStringContainsSearchValueIgnoringCase() {
        assertTrue(Utils.containsIgnoreCase("InjectByTypeAnnotationObjectConfigurator", "injectbytype"));
    }

    @Test
    public void shouldReturnFalseWhenStringDoesNotContainSearchValue() {
        assertFalse(Utils.containsIgnoreCase("ApplicationContext", "Service"));
    }

    @Test
    public void shouldReturnFalseWhenSourceStringIsNull() {
        assertFalse(Utils.containsIgnoreCase(null, "test"));
    }

    @Test
    public void shouldReturnFalseWhenSearchStringIsNull() {
        assertFalse(Utils.containsIgnoreCase("test", null));
    }

    @Test
    public void shouldReturnTrueWhenSearchStringIsEmpty() {
        assertTrue(Utils.containsIgnoreCase("test", ""));
    }
}