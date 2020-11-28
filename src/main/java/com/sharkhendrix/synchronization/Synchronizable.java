package com.sharkhendrix.synchronization;

public abstract class Synchronizable {

    private byte syncFlag;

    public byte getSyncFlag() {
        return syncFlag;
    }

    protected void requireSync() {
        requireSync(0);
    }

    protected void requireSync(int groupNumber) {
        syncFlag |= 1 << groupNumber;
    }
}
