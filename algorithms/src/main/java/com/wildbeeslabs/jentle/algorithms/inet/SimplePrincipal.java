package com.wildbeeslabs.jentle.algorithms.inet;

import java.io.Serializable;
import java.security.Principal;

/**
 * Simple principal implementation
 *
 * @author <a href="mailto:jesper.pedersen@ironjacamar.org">Jesper Pedersen</a>
 */
public class SimplePrincipal implements Principal, Serializable {
    private static final long serialVersionUID = -8553644554787699354L;

    /**
     * Principal name
     */
    private final String name;

    /**
     * Constructor
     *
     * @param name The principal name
     */
    public SimplePrincipal(String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (o == null || !(o instanceof Principal))
            return false;

        Principal p = (Principal) o;

        if (name == null) {
            return p.getName() == null;
        } else {
            return name.equals(p.getName());
        }
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return name == null ? 7 : name.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return name;
    }
}
