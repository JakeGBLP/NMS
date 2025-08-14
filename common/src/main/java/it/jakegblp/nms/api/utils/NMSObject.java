package it.jakegblp.nms.api.utils;

@FunctionalInterface
public interface NMSObject<T> {
    T asNMS();
}