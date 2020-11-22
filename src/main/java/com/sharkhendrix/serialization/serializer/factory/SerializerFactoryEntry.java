package com.sharkhendrix.serialization.serializer.factory;

import java.util.function.Function;
import java.util.function.Predicate;

import com.sharkhendrix.serialization.Serializer;
import com.sharkhendrix.serialization.serializer.ConfigurationNode;

class SerializerFactoryEntry {

    private Predicate<ConfigurationNode> condition;
    private Function<ConfigurationNode, Serializer<?>> builder;

    public SerializerFactoryEntry(Predicate<ConfigurationNode> condition, Function<ConfigurationNode, Serializer<?>> builder) {
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
