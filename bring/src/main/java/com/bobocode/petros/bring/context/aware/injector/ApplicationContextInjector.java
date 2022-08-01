package com.bobocode.petros.bring.context.aware.injector;

import com.bobocode.petros.bring.context.ApplicationContext;
import com.bobocode.petros.bring.context.aware.ApplicationContextAware;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

/**
 * Implementation of {@link Injector} that is responsible for injecting {@link ApplicationContext}
 * into the classes that are marked with {@link ApplicationContextAware}.
 */
public class ApplicationContextInjector implements Injector<ApplicationContext> {

    private ApplicationContext applicationContext;

    private ApplicationContextInjector() {}

    /**
     * {@inheritDoc}
     *
     * @param targetClass target class
     * @return class with injected object instance
     */
    @Override
    public Object inject(final Object targetClass) {
        Objects.requireNonNull(targetClass, "Target class must not be null!");
        Objects.requireNonNull(applicationContext, "Injectable type must not be null!");
        try {
            final Field applicationContextField = getApplicationContextField(targetClass.getClass());
            applicationContextField.setAccessible(true);
            applicationContextField.set(targetClass, applicationContext);
            return targetClass;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param injectableType injectable type
     */
    @Override
    public void setInjectableType(ApplicationContext injectableType) {
        Objects.requireNonNull(injectableType, "It's not allowed to set null as injectable type!");
        this.applicationContext = injectableType;
    }

    /**
     * {@inheritDoc}
     *
     * @return injectable typea
     */
    @Override
    public ApplicationContext getInjectableType() {
        Objects.requireNonNull(this.applicationContext, "Application context must be set before using it!");
        return this.applicationContext;
    }

    /**
     * Searches for a field of type {@link ApplicationContext}. Returns a field if it exists or throws an exception
     * otherwise.
     *
     * @param contextAwareClass a class that is context aware, e.g. (implementing {@link ApplicationContextAware}
     * @return field of type {@link ApplicationContext}
     */
    private Field getApplicationContextField(final Class<?> contextAwareClass) {
        return Arrays.stream(contextAwareClass.getDeclaredFields())
                .filter(field -> field.getType().isAssignableFrom(ApplicationContext.class))
                .findAny()
                .orElseThrow(() -> new IllegalStateException(
                        String.format(
                                "Field of type '%s' not found in class '%s'",
                                ApplicationContext.class.getName(), contextAwareClass.getName()
                        )
                ));
    }

    public static Injector<ApplicationContext> getInstance() {
        return ApplicationContextInjectorHolder.getContextInjector();
    }

    private enum ApplicationContextInjectorHolder {

        ;

        private static final Injector<ApplicationContext> CONTEXT_INJECTOR = new ApplicationContextInjector();

        ApplicationContextInjectorHolder() {
        }

        public static Injector<ApplicationContext> getContextInjector() {
            return CONTEXT_INJECTOR;
        }
    }
}
