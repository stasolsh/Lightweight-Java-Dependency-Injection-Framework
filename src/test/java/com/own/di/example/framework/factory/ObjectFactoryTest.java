package com.own.di.example.framework.factory;

import com.own.di.example.framework.context.ApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import javax.annotation.PostConstruct;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ObjectFactoryTest {

    private ApplicationContext context;

    @BeforeEach
    public void setUp() {
        context = mock(ApplicationContext.class);

        Reflections reflections =
                new Reflections("com.own.di.example.framework.factory");

        when(context.getScanner()).thenReturn(reflections);
    }

    @Test
    public void shouldCreateObjectUsingNoArgsConstructor() throws Exception {
        ObjectFactory factory = new ObjectFactory(context);

        SimpleService service = factory.createObject(SimpleService.class);

        assertNotNull(service);
        assertEquals("created", service.value());
    }

    @Test
    public void shouldInvokePostConstructMethod() throws Exception {
        ObjectFactory factory = new ObjectFactory(context);

        ServiceWithPostConstruct service =
                factory.createObject(ServiceWithPostConstruct.class);

        assertTrue(service.isInitialized());
    }

    @Test
    public void shouldReturnProxyForDeprecatedClass() throws Exception {
        ObjectFactory factory = new ObjectFactory(context);

        DeprecatedServiceApi service =
                factory.createObject(DeprecatedService.class);

        assertTrue(java.lang.reflect.Proxy.isProxyClass(service.getClass()));

        assertEquals(
                "deprecated-service-result",
                service.execute()
        );
    }

    public static class SimpleService {
        public String value() {
            return "created";
        }
    }

    public static class ServiceWithPostConstruct {

        private boolean initialized;

        @PostConstruct
        public void init() {
            initialized = true;
        }

        public boolean isInitialized() {
            return initialized;
        }
    }

    public interface DeprecatedServiceApi {
        String execute();
    }

    @Deprecated
    public static class DeprecatedService
            implements DeprecatedServiceApi {

        @Override
        public String execute() {
            return "deprecated-service-result";
        }
    }
}