package com.sharkhendrix.util;

public class UnsupportedComponentTypeHierarchyException extends Exception {

    private static final long serialVersionUID = 1L;

    public UnsupportedComponentTypeHierarchyException(String message) {
        super(message);
    }

    public UnsupportedComponentTypeHierarchyException(Throwable cause) {
        super(cause);
    }
}
