package it.jakegblp.nms.api.entity.metadata;

public interface BaseProperty {
    int getIndex();

    default Class<?> getType() {
        return getSerializerInfo().tClass();
    }

    String getName();

    EntityDataSerializerInfo<?> getSerializerInfo();
}
