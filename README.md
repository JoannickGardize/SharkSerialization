# SharkSerialization

SharkSerialization is an object graph serialization & synchronization framework with the goal of being super efficient, but as most easy to use as possible.

## Quickstart

Let's take a simple example : 
```java
import java.nio.ByteBuffer;

import com.sharkhendrix.serialization.SharkSerialization;

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
        serialization.register(A.class, A::new);
        serialization.register(B.class, B::new);
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
The main serialization class requires registration of every classes of the graph. Constructors are provided manually to avoid the very costly `Constructor.newInstance()` call.
The data is 6 bytes length : 4 bytes for the integer, and  2 bytes of overhead for the root object's type.

## Complex graphs

To improve efficiency, SharkSerialization considers by default the graph to be simple: fields are always instances of the declaring type, and there is no multiple references to the same instance. Fields which does not respect one, the other, or both of these conditions must be annotated with `@UndefinedType` and / or `@SharedReference`. Each of these annotions adds 2 bytes of overhead each time the field has to be serialized. Shared references are serialized only one time, other references will only takes 2 bytes.

## Incoming feature
Synchronization: share a know object graph and be able to only serialize modified parts.
