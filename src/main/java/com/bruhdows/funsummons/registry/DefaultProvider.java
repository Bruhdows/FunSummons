package com.bruhdows.funsummons.registry;

public interface DefaultProvider<T> {
    T[] getDefaults();
    String getId(T config);
    default String getFileName(T config) {
        return getId(config) + ".json";
    }
}