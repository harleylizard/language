package com.harleylizard.language;

public final class Reference<T> {
    private T t;

    public Reference(T t) {
        this.t = t;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
