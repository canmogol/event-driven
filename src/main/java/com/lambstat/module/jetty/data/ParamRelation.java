package com.lambstat.module.jetty.data;

public enum ParamRelation {
    EQ("equal"),
    LIKE("like"),
    NE("not equal"),
    BETWEEN("between"),
    GT("greater than"),
    GE("greater than or equal to"),
    LT("less than"),
    LE("less than or equal to");

    private String name;

    private ParamRelation(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getOrdinal() {
        return this.ordinal();
    }
}

