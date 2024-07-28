package com.harleylizard.language;

public final class DoubleReference {
    private double d;

    public DoubleReference(double d) {
        this.d = d;
    }

    public double getValue() {
        return d;
    }

    public void setValue(double d) {
        this.d = d;
    }
}
