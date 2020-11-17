package com.sharkhendrix.serialization.util;

@Deprecated
public class Record<T> {

    private int id;

    private T element;

    public Record(int id, T element) {
        super();
        this.id = id;
        this.element = element;
    }

    public int getId() {
        return id;
    }

    public T getElement() {
        return element;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return id == ((Record<T>) obj).id;
    }

}
