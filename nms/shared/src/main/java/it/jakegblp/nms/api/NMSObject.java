package it.jakegblp.nms.api;

@FunctionalInterface
public interface NMSObject<T> {

    T asNMS();
}