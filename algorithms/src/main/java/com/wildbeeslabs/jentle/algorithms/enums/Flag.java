package com.wildbeeslabs.jentle.algorithms.enums;

/**
 * @author Christoph Strobl
 * @since 1.7
 */
public enum Flag {

    MYSELF("myself"), MASTER("master"), SLAVE("slave"), FAIL("fail"), PFAIL("fail?"), HANDSHAKE("handshake"), NOADDR(
        "noaddr"), NOFLAGS("noflags");

    private String raw;

    Flag(String raw) {
        this.raw = raw;
    }

    public String getRaw() {
        return raw;
    }

}
