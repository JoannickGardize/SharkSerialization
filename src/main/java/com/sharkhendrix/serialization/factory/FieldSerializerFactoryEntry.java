package com.sharkhendrix.serialization.factory;

import java.util.function.Function;
import java.util.function.Predicate;

import com.sharkhendrix.serialization.Serializer;

class FieldSerializerFactoryEntry {

    private Predicate<FieldConfiguration> condition;
    private Function<FieldConfiguration, Serializer<?>> builder;

    public FieldSerializerFactoryEntry(Predicate<FieldConfiguration> condition, Function<FieldConfiguration, Serializer<?>> builder) {
        this.condition = condition;
        this.builder = builder;
    }

    public boolean test(FieldConfiguration field) {
        return condition.test(field);
    }

    public Serializer<?> build(FieldConfiguration field) {
        return builder.apply(field);
    }

}
