package com.sharkhendrix.util;

import java.util.Objects;

import com.sharkhendrix.serialization.annotation.VarLenStrategy;

public class VarLenTypeKey {

    private Class<?> type;

    private VarLenStrategy varLenStrategy;

    public VarLenTypeKey(Class<?> type, VarLenStrategy varLenStrategy) {
        this.type = type;
        this.varLenStrategy = varLenStrategy;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, varLenStrategy);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof VarLenTypeKey)) {
            return false;
        }
        VarLenTypeKey other = (VarLenTypeKey) obj;
        return Objects.equals(type, other.type) && varLenStrategy == other.varLenStrategy;
    }
}
