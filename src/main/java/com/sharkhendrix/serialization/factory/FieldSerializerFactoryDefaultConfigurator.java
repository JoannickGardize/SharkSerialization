package com.sharkhendrix.serialization.factory;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.IntFunction;

import com.sharkhendrix.serialization.SerializationContext;
import com.sharkhendrix.serialization.Serializer;
import com.sharkhendrix.serialization.SharkSerializationException;
import com.sharkhendrix.serialization.annotation.AnnotationConfigurationFactory;
import com.sharkhendrix.serialization.serializer.ArraySerializer;
import com.sharkhendrix.serialization.serializer.CollectionSerializer;
import com.sharkhendrix.serialization.serializer.MapSerializer;
import com.sharkhendrix.serialization.serializer.SharedReferenceSerializer;
import com.sharkhendrix.serialization.serializer.UndefinedTypeSerializer;
import com.sharkhendrix.serialization.util.ReflectionUtils;

public class FieldSerializerFactoryDefaultConfigurator {

    private SerializationContext context;

    public FieldSerializerFactoryDefaultConfigurator(SerializationContext context) {
        this.context = context;
    }

    public void configure() {
        FieldSerializerFactory factory = context.getFieldSerializerFactory();
        factory.setFieldConfigurator(f -> ConfigurationNodeTypeMerger.merge(AnnotationConfigurationFactory.build(f), ReflectionUtils.getComponentTypeHierarchy(f)));
        factory.setDefault(this::defaultBuilder);
        factory.addCase("array", n -> n.getType() != null && n.getType().isArray() && !n.getType().getComponentType().isPrimitive(), this::nonPrimitiveArrayBuilder);
        factory.addCase("collection", n -> n.getType() != null && Collection.class.isAssignableFrom(n.getType()), this::collectionBuilder);
        factory.addCase("map", n -> n.getType() != null && Map.class.isAssignableFrom(n.getType()), this::mapBuilder);
    }

    private Serializer<?> defaultBuilder(ConfigurationNode fieldConfiguration) {
        return defaultBuilder(fieldConfiguration.getType(), fieldConfiguration);
    }

    private <T> Serializer<? extends T> defaultBuilder(Class<T> type, ConfigurationNode configuration) {
        Serializer<? extends T> serializer;
        if (configuration.isUndefinedType()) {
            serializer = new UndefinedTypeSerializer<>(context);
        } else {
            serializer = context.getSerializer(type);
        }
        return decorateSharedReference(configuration, serializer);
    }

    private Serializer<?> nonPrimitiveArrayBuilder(ConfigurationNode fieldConfiguration) {
        return collectionBuilder(fieldConfiguration, (t, u) -> new ArraySerializer<>(t, u));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Serializer<?> collectionBuilder(ConfigurationNode fieldConfiguration) {
        return collectionBuilder(fieldConfiguration, (t, u) -> new CollectionSerializer(t, u));
    }

    private Serializer<?> collectionBuilder(ConfigurationNode configurationNode, BiFunction<IntFunction<?>, Serializer<?>, Serializer<?>> serializerConstructor) {
        FieldSerializerFactory factory = context.getFieldSerializerFactory();
        IntFunction<?> constructor = commonChecksAndGetConstructor(configurationNode, factory);
        return decorateSharedReference(configurationNode, serializerConstructor.apply(constructor, factory.build(configurationNode.getElementsConfiguration())));
    }

    @SuppressWarnings("unchecked")
    private Serializer<?> mapBuilder(ConfigurationNode configurationNode) {
        FieldSerializerFactory factory = context.getFieldSerializerFactory();
        IntFunction<? extends Map<Object, Object>> constructor = (IntFunction<? extends Map<Object, Object>>) commonChecksAndGetConstructor(configurationNode, factory);
        return decorateSharedReference(configurationNode,
                new MapSerializer<>(constructor, factory.build(configurationNode.getKeysConfiguration()), factory.build(configurationNode.getValuesConfiguration())));
    }

    private IntFunction<?> commonChecksAndGetConstructor(ConfigurationNode configurationNode, FieldSerializerFactory factory) {
        if (configurationNode.isUndefinedType()) {
            throw new SharkSerializationException("UndefinedType is unsupported by the field factory for arrays, collections, and maps.");
        }
        IntFunction<?> constructor = factory.getSizeableConstructorRecord(configurationNode.getType());
        if (constructor == null) {
            throw new SharkSerializationException("Missing registered constructor for " + configurationNode.getType().getName());
        }
        return constructor;
    }

    private <T> Serializer<? extends T> decorateSharedReference(ConfigurationNode configuration, Serializer<? extends T> serializer) {
        if (configuration.isSharedReference()) {
            return new SharedReferenceSerializer<>(context.getReferenceContext(), serializer);
        } else {
            return serializer;
        }
    }
}
