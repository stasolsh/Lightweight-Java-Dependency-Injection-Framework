package com.own.di.example.framework.context;

import com.own.di.example.framework.annotation.Singleton;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationContextTest {

    private final ApplicationContext context =
            new ApplicationContext("com.own.di.example.framework.context");

    @Test
    public void shouldCreateObjectForConcreteClass() {
        TestService service = context.getObject(TestService.class);

        assertNotNull(service);
    }

    @Test
    public void shouldReturnNewInstanceForNonSingletonBean() {
        TestService first = context.getObject(TestService.class);
        TestService second = context.getObject(TestService.class);

        assertNotSame(first, second);
    }

    @Test
    public void shouldReturnSameInstanceForSingletonBean() {
        SingletonService first = context.getObject(SingletonService.class);
        SingletonService second = context.getObject(SingletonService.class);

        assertSame(first, second);
    }

    @Test
    public void shouldResolveImplementationByInterface() {
        TestInterface service = context.getObject(TestInterface.class);

        assertNotNull(service);
        assertInstanceOf(TestInterfaceImpl.class, service);
    }

    @Test
    public void shouldResolveSingletonImplementationByInterface() {
        SingletonInterface first = context.getObject(SingletonInterface.class);
        SingletonInterface second = context.getObject(SingletonInterface.class);

        assertSame(first, second);
    }

    @Test
    public void shouldResolveQualifiedImplementation() {
        QualifiedInterface service =
                context.getObject(QualifiedInterface.class, "special");

        assertNotNull(service);
        assertInstanceOf(SpecialQualifiedService.class, service);
    }

    public static class TestService {
    }

    @Singleton
    public static class SingletonService {
    }

    public interface TestInterface {
    }

    public static class TestInterfaceImpl implements TestInterface {
    }

    public interface SingletonInterface {
    }

    @Singleton
    public static class SingletonInterfaceImpl implements SingletonInterface {
    }

    public interface QualifiedInterface {
    }

    @Singleton(qualifier = "special")
    public static class SpecialQualifiedService implements QualifiedInterface {
    }

    interface QualifiedSingletonInterface {
    }

    @Singleton(qualifier = "special")
    static class QualifiedSingletonImpl implements QualifiedSingletonInterface {
    }
}