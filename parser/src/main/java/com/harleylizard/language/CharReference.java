package com.harleylizard.language;

public final class CharReference {
    private char c;

    public CharReference(char c) {
        this.c = c;
    }

    public char getValue() {
        return c;
    }

    public void setValue(char c) {
        this.c = c;
    }
}
