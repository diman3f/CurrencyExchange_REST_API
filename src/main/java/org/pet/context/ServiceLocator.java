package org.pet.context;

import java.util.HashMap;
import java.util.Map;

public final class ServiceLocator {
    private static final Map<Class<?>, Object> services = new HashMap<>();

    private ServiceLocator() {
        throw new AssertionError("Нельзя создать объект статического класса");
    }

    public static <T> Object getService(Class<T> serviceClass) {
        return serviceClass.cast(services.get(serviceClass));
    }

    public static void registerService(Object service) {
        services.put(service.getClass(), service);
    }


}
