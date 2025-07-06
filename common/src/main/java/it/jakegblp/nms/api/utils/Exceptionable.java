package it.jakegblp.nms.api.utils;

public interface Exceptionable<E extends Exception> {
    void validate() throws E;
}
