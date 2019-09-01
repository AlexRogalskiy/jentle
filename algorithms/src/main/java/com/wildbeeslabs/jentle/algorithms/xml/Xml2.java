package com.wildbeeslabs.jentle.algorithms.xml;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Objects;

@Data
@EqualsAndHashCode
@ToString
public class Xml2 {
    /*
      "name": { "type": "string"},
      "namespace": { "type": "string" },
      "prefix": { "type": "string" },
      "attribute": { "type": "boolean" },
      "wrapped": { "type": "boolean" }
    */
    private String name;
    private String namespace;
    private String prefix;
    private Boolean attribute;
    private Boolean wrapped;

    public Xml2 name(final String name) {
        this.setName(name);
        return this;
    }

    public Xml2 namespace(final String namespace) {
        this.setNamespace(namespace);
        return this;
    }

    public Xml2 prefix(final String prefix) {
        this.setPrefix(prefix);
        return this;
    }

    public Xml2 attribute(final Boolean attribute) {
        this.setAttribute(attribute);
        return this;
    }

    public Xml2 wrapped(final Boolean wrapped) {
        this.setWrapped(wrapped);
        return this;
    }

    @Override
    public Object clone() {
        return new Xml2()
            .attribute(Objects.isNull(this.attribute) ? null : Boolean.valueOf(attribute))
            .name(name)
            .namespace(namespace)
            .prefix(prefix)
            .wrapped(Objects.isNull(this.wrapped) ? null : Boolean.valueOf(wrapped));
    }
}
