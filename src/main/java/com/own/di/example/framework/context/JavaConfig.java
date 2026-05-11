package com.own.di.example.framework.context;

import com.own.di.example.framework.interfaces.Config;
import com.own.di.example.framework.util.Utils;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.util.Objects;

import static java.util.Arrays.stream;

/**
 * The JavaConfig class provides configuration settings for the application context.
 */
@AllArgsConstructor
public class JavaConfig implements Config {
    private Reflections scanner;
    private Ifc2ImplHolder ifc2ImplHolder;


    @Override
    @SneakyThrows
    public <T> Class<? extends T> getImplClass(Class<T> type, String... qualifiers) {
        Class<T> objectFromHolder = null;
        boolean hasQualifiers = stream(qualifiers).anyMatch(Objects::nonNull);
        if (hasQualifiers && ifc2ImplHolder.containsKey(type, qualifiers[0])) {
            objectFromHolder = ifc2ImplHolder.get(type, qualifiers[0]);
        } else if (ifc2ImplHolder.containsKey(type) && !hasQualifiers) {
            objectFromHolder = ifc2ImplHolder.get(type);
        }

        return Objects.nonNull(objectFromHolder) ? objectFromHolder : getaClassByReflections(type, qualifiers, hasQualifiers);
    }

    private <T> Class<? extends T> getaClassByReflections(Class<T> type, String[] qualifiers, boolean hasQualifiers) {
        Class<? extends T> clazz;
        if (hasQualifiers) {
            clazz = scanner.getSubTypesOf(type).stream().filter(it ->
                    Utils.containsIgnoreCase(it.getName(), qualifiers[0])).findAny().orElseThrow(() ->
                    new RuntimeException("There is not class with type " + type + "and qualifier " + qualifiers[0]));
            ifc2ImplHolder.add(Ifc2Impl.builder().ifc(type).impl(clazz).alias(qualifiers[0]).build());
        } else {
            clazz = scanner.getSubTypesOf(type).iterator().next();
            ifc2ImplHolder.add(Ifc2Impl.builder().ifc(type).impl(clazz).build());
        }
        return clazz;
    }
}
