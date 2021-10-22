package com.sharkhendrix.serialization.annotation;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.sharkhendrix.serialization.annotation.ElementsConfiguration.Type;
import com.sharkhendrix.serialization.serializer.ConfigurationNode;

public class AnnotationConfigurationFactoryTest {

	@ConcreteType(ArrayList.class)
	@SharedReference
	@ElementsConfiguration(concreteType = HashMap.class)
	@ElementsConfiguration(type = Type.KEYS, concreteType = Integer.class, sharedReference = true)
	@ElementsConfiguration(concreteType = Character.class)
	@ElementsConfiguration(concreteType = Short.class)
	@ElementsConfiguration(type = Type.VALUES, concreteType = Byte.class)
	@ElementsConfiguration(type = Type.KEYS, concreteType = Double.class)
	@ElementsConfiguration(type = Type.VALUES, concreteType = Long.class)
	private Object field;

	@Test
	public void buildTest() throws NoSuchFieldException, SecurityException {
		ConfigurationNode node = AnnotationConfigurationFactory.build(getClass().getDeclaredField("field"));

		Assertions.assertEquals(node.getType(), ArrayList.class);
		Assertions.assertTrue(node.isSharedReference());
		Assertions.assertFalse(node.isUndefinedType());
		Assertions.assertNull(node.getKeysConfiguration());
		Assertions.assertNull(node.getValuesConfiguration());

		node = node.getElementsConfiguration();

		Assertions.assertEquals(node.getKeysConfiguration().getType(), Integer.class);
		Assertions.assertTrue(node.getKeysConfiguration().isSharedReference());
		Assertions.assertFalse(node.getKeysConfiguration().isUndefinedType());
		Assertions.assertNull(node.getKeysConfiguration().getKeysConfiguration());
		Assertions.assertNull(node.getKeysConfiguration().getValuesConfiguration());
		Assertions.assertEquals(node.getKeysConfiguration().getElementsConfiguration().getType(), Character.class);
		Assertions.assertEquals(node.getKeysConfiguration().getElementsConfiguration().getElementsConfiguration().getType(), Short.class);

		Assertions.assertEquals(node.getValuesConfiguration().getType(), Byte.class);
		Assertions.assertEquals(node.getValuesConfiguration().getKeysConfiguration().getType(), Double.class);
		Assertions.assertEquals(node.getValuesConfiguration().getValuesConfiguration().getType(), Long.class);
	}
}
