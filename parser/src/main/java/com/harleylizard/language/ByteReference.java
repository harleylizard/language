package com.harleylizard.language;

public final class ByteReference {
    private byte b;

    public ByteReference(byte b) {
        this.b = b;
    }

    public byte getValue() {
        return b;
    }

    public void setValue(byte b) {
        this.b = b;
    }
}
