package com.harleylizard.language;

public final class Reference<T> {
    private T t;

    public Reference(T t) {
        this.t = t;
    }

    public T getValue() {
        return t;
    }

    public void setValue(T t) {
        this.t = t;
    }
}
