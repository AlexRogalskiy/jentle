package com.wildbeeslabs.jentle.algorithms.toolset;

import java.io.Serializable;

public class ParameterInfo implements Serializable {

    public enum Type {
        NONE("") {
            @Override
            public String prepend(String value, Character separator) {
                return value;
            }
        },
        DATASOURCE("ds"),
        COMPONENT("component"),
        PARAM("param"),
        SESSION("session"),
        CUSTOM("custom");

        private String prefix;

        Type(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }

        public String prepend(String value, Character separator) {
            return prefix + separator + value;
        }
    }

    protected Type type;
    protected String path;
    protected Class javaClass;
    protected String conditionName;
    protected boolean caseInsensitive;
    protected String value;

    ParameterInfo(String name, Type type, boolean caseInsensitive) {
        this.path = name;
        this.type = type;
        this.caseInsensitive = caseInsensitive;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return type.prepend(path, '$');
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFlatName() {
        return type.prepend(path, '.').replace(".", "_");
    }

    public boolean isCaseInsensitive() {
        return caseInsensitive;
    }

    public Class getJavaClass() {
        return javaClass;
    }

    public void setJavaClass(Class javaClass) {
        this.javaClass = javaClass;
    }

    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParameterInfo that = (ParameterInfo) o;

        return path.equals(that.path) && type == that.type;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + path.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return type + " : " + path;
    }
}
