## Development progress

# SharkSerialization

SharkSerialization is an object graph serialization framework with the goal of being super efficient in terms of execution time and data size, while being as most easy to use as possible.

The target usage is to share real time based data. It is not recommanded for long term data storage, since any structural changes in a class of the graph will break readability of a previously serialized data.

## Getting started

Let's take a simple example : 
```java
public class GettingStarted {

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
        System.out.println("data size : " + buffer.limit() + " bytes");

        A a2 = (A) serialization.read(buffer);
        System.out.println("data : " + a2.b.data);
    }
}
```
Console output :
```console
data size : 2 bytes
data : 42
```
SharkSerialization requires registration of every classes of the graph, the order is important and must be the same for serialization and deserialization. Constructors are provided manually to avoid the costly `Constructor.newInstance()` call.
The data is 2 bytes length : 1 byte for the integer (variable-length compression), and  1 byte of overhead for the root object's type.

## Graph configuration

### Fields configuration

The philosophy of SharkSerialization is to considers the graph to be simple by default, any complexity will require configuration:
- Fields declared type and actual instance type always matches together, or at least matches a registered class and serializer pair, and are never null. If this is not the case for a class field, annotate it with `@UndefinedType` (at least +1 byte of overhead). This is also possible to specify the value type if it is always the same with `@ConcreteType` (So you'll never have `@UndefinedType` and `@ConcreteType` in the same field).
- There is no multiple references to the same instance. If this is the case, annotate the field with `@SharedReference` (at least +1 byte of overhead for the first occurence, following occurences will only take 1 or more bytes), any field that possibly shares it's reference should be annotated with `@SharedReference`.

### Fields with arrays, collections and maps configuration

All container types (Arrays, Collections and Maps) are supported and, like field, are considered to be simple by default. Container type must be defined, by the field declaration or with `@ConcreteType`, and it's constructor must be registered by calling `SharkSerialization.registerConstructor(type, constructor)` (unless it is by default, see the last chapter).

The annotation `ElementsConfiguration` allows you to configure the container elements. For example, if a list contains elements that are referenced multiple times anywhere in the graph, it will be configured as following:

```java
class ListExample {

    @ElementsConfiguration(sharedReference = true)
    private List<AnotherClass> myList;
}
```

For map configuration, if you configure it, it must have an `@ElementsConfiguration(type = ElementsConfigurationType.KEYS)` followed by an `@ElementsConfiguration(type = ElementsConfigurationType.VALUES)`. Any hierarchy of arrays, collections and maps can be configured by putting successive `@ElementsConfiguration`, and any branch of the hierarchy can be omitted if the field declaration and the default configuration is enough. Let's take an example:

```java
class MapExample {

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

#### Register containers constructors

Containers are treated specifically by the SerializerFactory (except for primitive arrays). They do not require registration of serializers but require registration of their constructors. For any non-default configured (see next chapter) array, collection or map types, a call to `SharkSerialization.registerConstructor(type, constructor)` is required to register them.

### Methods of configuration

The previous examples of this chapter used annotation configuration, this is also possible to directly configure the serializer instead, for example, the equivalent of the previous map example would be:

```java
serialization.registerObject(MapExample.class, MapExample::new).configure("map")
    .sharedReference()
    .keys().concreteType(List.class)
    .elements().concreteType(MyClass.class).sharedReference()
    .values(); // Like annotations, values() must always be called after keys(), even if there is nothing to configure.

```

#### Priorities

According to the default SerializerFactory configuration, The priority of the different field's configuration methods is (individually for each field):

```
ObjectSerializer configuration > Annotation configuration > Field declaration (for the declared type)
```

## Default serialization configuration

Primitives, primitive wrappers, primitive arrays, strings, lists, and maps are configured by default:
- Default registered serializers: Primitive Wrappers (Integer, Character...), primitive arrays (int[], char[]....), String.
- Primitive types (int, char...) ar treated specifically at the field access level to use optimized field access methods (Field.getInt, Field.getChar...).
- Default container constructors: List -> ArrayList, Set -> HashSet, Map -> HashMap.


