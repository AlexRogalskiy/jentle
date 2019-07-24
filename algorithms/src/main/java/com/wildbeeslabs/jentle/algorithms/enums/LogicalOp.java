package com.wildbeeslabs.jentle.algorithms.enums;

public enum LogicalOp {
    AND("and", "&&"),
    OR("or", "||");

    private final String forJpql;
    private final String forGroovy;

    LogicalOp(String forJpql, String forGroovy) {
        this.forJpql = forJpql;
        this.forGroovy = forGroovy;
    }

    public String forJpql() {
        return forJpql;
    }

    public String forGroovy() {
        return forGroovy;
    }

    public static LogicalOp fromString(String str) {
        if (AND.forJpql().equals(str))
            return AND;
        else if (OR.forJpql().equals(str))
            return OR;
        else
            throw new IllegalArgumentException("Invalid LogicalOp: " + str);
    }
}
