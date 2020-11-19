package com.sharkhendrix.serialization.util;

public class UnsupportedComponentTypeHierarchyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnsupportedComponentTypeHierarchyException(String message) {
        super(message);
    }

    public UnsupportedComponentTypeHierarchyException(Throwable cause) {
        super(cause);
    }
}
