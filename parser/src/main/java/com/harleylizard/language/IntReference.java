package com.harleylizard.language;

public final class IntReference {
    private int i;

    public IntReference(int i) {
        this.i = i;
    }

    public int getValue() {
        return i;
    }

    public void setValue(int i) {
        this.i = i;
    }
}
