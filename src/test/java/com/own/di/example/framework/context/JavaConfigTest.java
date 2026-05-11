package com.own.di.example.framework.context;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import static org.junit.jupiter.api.Assertions.*;

public class JavaConfigTest {

    @Test
    public void shouldResolveImplementationFromHolderWithoutQualifier() {
        Ifc2ImplHolder<TestApi> holder = new Ifc2ImplHolder<>();

        holder.add(Ifc2Impl.builder()
                .ifc(TestApi.class)
                .impl(TestApiImpl.class)
                .build());

        JavaConfig config = new JavaConfig(new Reflections("com.own.di.example.framework.context"), holder);

        Class<? extends TestApi> result = config.getImplClass(TestApi.class);

        assertEquals(TestApiImpl.class, result);
    }

    @Test
    public void shouldResolveImplementationFromHolderWithQualifier() {
        Ifc2ImplHolder<TestApi> holder = new Ifc2ImplHolder<>();

        holder.add(Ifc2Impl.builder()
                .ifc(TestApi.class)
                .impl(SpecialTestApiImpl.class)
                .alias("special")
                .build());

        JavaConfig config = new JavaConfig(new Reflections("com.own.di.example.framework.context"), holder);

        Class<? extends TestApi> result = config.getImplClass(TestApi.class, "special");

        assertEquals(SpecialTestApiImpl.class, result);
    }

    @Test
    public void shouldThrowExceptionWhenQualifiedImplementationCannotBeFound() {
        Ifc2ImplHolder<TestApi> holder = new Ifc2ImplHolder<>();
        JavaConfig config = new JavaConfig(new Reflections("com.own.di.example.framework.context"), holder);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> config.getImplClass(TestApi.class, "missing")
        );

        assertTrue(exception.getMessage().contains("There is not class with type"));
    }

    interface TestApi {
    }

    static class TestApiImpl implements TestApi {
    }

    static class SpecialTestApiImpl implements TestApi {
    }
}