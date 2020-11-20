## Development progress

*The development of this framework is still in progress*

- [X] Object & primitives serializarion
- [X] Collections & Maps serialization
- [X] Getter/Setter Serializer alternative
- [ ] Synchronization framework

# SharkSerialization

SharkSerialization is an object graph serialization & synchronization framework with the goal of being super efficient in terms of execution time and data size, while being as most easy to use as possible.

The target usage is to share real time based data. It is not recommanded for long term data storage, since any structural changes in a class of the graph will break readability of a previously serialized data.

## Quickstart

Let's take a simple example : 
```java
public class Quickstart {

    static class A {
        B b;
    }

    static class B {
        int data;
    }

    public static void main(String[] args) {

        // Initialize the serializer
        SharkSerialization serialization = new SharkSerialization();
        serialization.registerObject(A.class, A::new);
        serialization.registerObject(B.class, B::new);
        serialization.initialize();

        // Build data
        A a = new A();
        a.b = new B();
        a.b.data = 42;

        // Serialize data and deserialize data
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        serialization.write(buffer, a);

        buffer.flip();
        System.out.println("data size : " + buffer.limit());

        A deserializedA = (A) serialization.read(buffer);
        System.out.println("message : " + deserializedA.b.data);
    }
}
```
Console output :
```console
data size : 6
message : 42
```
SharkSerialization requires registration of every classes of the graph, the order is important and must be the same for serialization and deserialization. Constructors are provided manually to avoid the costly `Constructor.newInstance()` call.
The data is 6 bytes length : 4 bytes for the integer, and  2 bytes of overhead for the root object's type.

## Graph configuration

### Fields configuration

To improve efficiency, SharkSerialization considers the graph to be simple by default, any complexity will require configuration:
- Fields declared type and actual instance type always matches together, or at least matches a registered class and serializer pair, and are never null. If this is not the case for a class field, annotate it with `@UndefinedType` (+2 bytes of overhead). It is also possible to specify the value type if it is always the same with `@ConcreteType`.
- There is no multiple references to the same instance. If this is the case, annotate the field with `@SharedReference` (+2 bytes of overhead for the first occurence, following occurences will only take 2 bytes), any field that possibly shares it's reference should be annotated with `@SharedReference`.

### Arrays, Collections and Maps configuration

All container types (Arrays, Collections and Maps) are supported and, like field, are considered to be simple by default. Container type must be defined, by the field declaration or with `@ConcreteType`, and it's constructor must be registered by calling `SharkSerialization.registerConstructor(type, constructor)` (unless it is by default, see the last chapter).

The annotation `ElementsConfiguration` allows you to configure the container elements. For example, if a list contains elements that are referenced multiple times anywhere in the graph, it will be configured as following:

```java
class MyClass {

    @ElementsConfiguration(sharedReference = true)
    private List<AnotherClass> myList;
}
```

For map configuration, if you configure it, it must have an `@ElementsConfiguration(type = ElementsConfigurationType.KEYS)` followed by an `@ElementsConfiguration(type = ElementsConfigurationType.VALUES)`. Any hierarchy of arrays, collections and maps can be configured by putting successive `@ElementsConfiguration`, and any branch of the hierarchy can be omitted if the field declaration and the default configuration is enough. Let's take an example:

```java
class MyClass {

    @SharedReference
    @ElementsConfiguration(type = ElementsConfigurationType.KEYS, concreteType = List.class)
    @ElementsConfiguration(concreteType = MyClass.class, sharedReference = true)
    @ElementsConfiguration(type = ElementsConfigurationType.VALUES)
    Map<Object, Map<String, String>> map;
}
```

Let's read annotations line by line:
- `@SharedReference`: the map itself is declared to be shared elsewhere in the graph
- `@ElementsConfiguration(type = ElementsConfigurationType.KEYS, concreteType = List.class)`: configure the key type of the map, they are lists, so the following annotation will configure it's elements.
- `@ElementsConfiguration(concreteType = MyClass.class, sharedReference = true)`: configure the elements of the list, now the serializer knows that the keys of the map are `List<MyClass>`. Elements of this list are declared to be shared somewhere in the graph, maybe in the list itself.
- `@ElementsConfiguration(type = ElementsConfigurationType.VALUES)`: required as pair of the KEYS, but nothing to configure here, because the field's type declaration is enough (`Map<String, String>`).

#### Registered containers constructors

Containers are treated specifically by the SerializationFacotry (except for primitive arrays). They do not requires registration of serializers but requires registration of their constructors. Map references are binded by default with HashMap, and List with ArrayList. For any other array, collection or map types, call `SharkSerialization.registerConstructor(type, constructor)` to register them.

## Getter / Setter alternative

To (drastically) improve execution time at serialization an deserialization, Object serializers can be configured to use getters and setters methods instead of the default reflection field access. To achieve this, When registering a class, an `ObjectSerializerConfigurationHelper` is returned to configure the serializer in a "chaining" way.

```java
class ExampleClass {
    Something anObject;
    int anInteger;
    double aDouble;
    
    // Getter and Setter methods
    public void setAnObject() {
    [...]
}

// first way, configure them by field name
SharkSerialization serialization = new SharkSerialization();
serialization.register(ExampleClass.class, ExampleClass::new).access("anObject", ExampleClass::getAnObject, ExampleClass::setAnObject).primitiveAccess("aDouble", ExampleClass::getADouble, ExampleClass::setADouble).primitiveAccess("anInteger", ExampleClass::getAnInteger, ExampleClass::setAnInteger);

// Second way, configure them using their declaration order (eventual parent class fields in last position)
SharkSerialization serialization = new SharkSerialization();
serialization.register(ExampleClass.class, ExampleClass::new).access(ExampleClass::getAnObject, ExampleClass::setAnObject).primitiveAccess(ExampleClass::getAnInteger, ExampleClass::setAnInteger).primitiveAccess(ExampleClass::getADouble, ExampleClass::setADouble);
```

Note that primitive types must use `primitiveAccess()` and object types must use `access()`.

## Default configurations

Primitives, primitive wrappers, primitive arrays, strings, lists, and maps are configured by default:
- Default registered serializers: Primitive Wrappers (Integer, Character...), primitive arrays (int[], char[]....), String.
- Primitive types (int, char...) ar treated specifically at the field access level to use optimized field access methods (Field.getInt, Field.getChar...).
- Default container constructors: List -> ArrayList, Set -> HashSet, Map -> HashMap.


