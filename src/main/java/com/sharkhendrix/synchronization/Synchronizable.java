package com.sharkhendrix.synchronization;

public abstract class Synchronizable {

    private byte syncFlag;

    public byte getSyncFlag() {
        return syncFlag;
    }

    protected void requireSync() {
        syncFlag |= 1;
    }

    protected void requireSync(int syncChunkNumber) {
        syncFlag |= 1 << syncChunkNumber;
    }

}
