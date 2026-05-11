package com.own.di.example.framework.processing;

import com.own.di.example.framework.annotation.InjectByType;
import com.own.di.example.framework.annotation.support.Locale;
import com.own.di.example.framework.annotation.support.ValidationPatterns;
import com.own.di.example.framework.context.ApplicationContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InjectByTypeAnnotationObjectConfiguratorTest {

    @Test
    public void shouldInjectDependencyByType() {
        ApplicationContext context = mock(ApplicationContext.class);
        Dependency dependency = new Dependency();

        when(context.getObject(Dependency.class, "")).thenReturn(dependency);

        Target target = new Target();

        InjectByTypeAnnotationObjectConfigurator configurator =
                new InjectByTypeAnnotationObjectConfigurator();

        configurator.configure(target, context);

        assertSame(dependency, target.getDependency());
    }

    @Test
    public void shouldInjectDependencyByTypeAndQualifier() {
        ApplicationContext context = mock(ApplicationContext.class);
        Dependency dependency = new Dependency();

        when(context.getObject(Dependency.class, "special")).thenReturn(dependency);

        QualifiedTarget target = new QualifiedTarget();

        InjectByTypeAnnotationObjectConfigurator configurator =
                new InjectByTypeAnnotationObjectConfigurator();

        configurator.configure(target, context);

        assertSame(dependency, target.getDependency());
    }

    @Test
    public void shouldApplyLocaleAnnotationToInjectedDependency() {
        ApplicationContext context = mock(ApplicationContext.class);
        Dependency dependency = new Dependency();

        when(context.getObject(Dependency.class, "")).thenReturn(dependency);

        LocaleTarget target = new LocaleTarget();

        InjectByTypeAnnotationObjectConfigurator configurator =
                new InjectByTypeAnnotationObjectConfigurator();

        configurator.configure(target, context);

        assertSame(dependency, target.getDependency());
        assertEquals("ENGLISH", dependency.getLocale());
    }

    @Test
    public void shouldApplyValidationPatternsAnnotationToInjectedDependency() {
        ApplicationContext context = mock(ApplicationContext.class);
        Dependency dependency = new Dependency();

        when(context.getObject(Dependency.class, "")).thenReturn(dependency);

        ValidationTarget target = new ValidationTarget();

        InjectByTypeAnnotationObjectConfigurator configurator =
                new InjectByTypeAnnotationObjectConfigurator();

        configurator.configure(target, context);

        assertSame(dependency, target.getDependency());
        assertArrayEquals(new String[]{"\\d+", "[a-z]+"}, dependency.getPatterns());
    }

    private static class Target {
        @InjectByType
        private Dependency dependency;

        Dependency getDependency() {
            return dependency;
        }
    }

    static class QualifiedTarget {
        @InjectByType(qualifier = "special")
        private Dependency dependency;

        Dependency getDependency() {
            return dependency;
        }
    }

    static class LocaleTarget {
        @InjectByType
        @Locale("ENGLISH")
        private Dependency dependency;

        Dependency getDependency() {
            return dependency;
        }
    }

    static class ValidationTarget {
        @InjectByType
        @ValidationPatterns({"\\d+", "[a-z]+"})
        private Dependency dependency;

        Dependency getDependency() {
            return dependency;
        }
    }

    static class Dependency {
        private String locale;
        private String[] patterns;

        String getLocale() {
            return locale;
        }

        String[] getPatterns() {
            return patterns;
        }
    }
}