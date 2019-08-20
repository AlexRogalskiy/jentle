package com.wildbeeslabs.jentle.collections.graph2;

public class DexClass {
    private boolean innerClass;
    private boolean lambdaClass;
    private String dexFileName;
    private String name;
    private String type;
    private String superType;

    public String getType() {
        return type;
    }

    public String getSuperType() {
        return superType;
    }

    public boolean isInnerClass() {
        return innerClass;
    }

    public boolean isLambda() {
        return lambdaClass;
    }

    public String getSimpleName() {
        return name;
    }

    public String getDexFileName() {
        return dexFileName;
    }
}
