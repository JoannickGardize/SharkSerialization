package com.sharkhendrix.synchronization;

public interface SynchronizationView {

    boolean isKnown(Object o);

    boolean look(Object o);
}
