package com.sharkhendrix.serialization.factory;

import java.util.function.Function;
import java.util.function.Predicate;

import com.sharkhendrix.serialization.Serializer;

class FieldSerializerFactoryEntry {

    private Predicate<ConfigurationNode> condition;
    private Function<ConfigurationNode, Serializer<?>> builder;

    public FieldSerializerFactoryEntry(Predicate<ConfigurationNode> condition, Function<ConfigurationNode, Serializer<?>> builder) {
        this.condition = condition;
        this.builder = builder;
    }

    public boolean test(ConfigurationNode field) {
        return condition.test(field);
    }

    public Serializer<?> build(ConfigurationNode field) {
        return builder.apply(field);
    }

}
