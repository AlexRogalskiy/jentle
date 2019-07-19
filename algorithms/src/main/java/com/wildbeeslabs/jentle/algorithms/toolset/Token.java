package com.wildbeeslabs.jentle.algorithms.toolset;

import lombok.Data;

@Data
public abstract class Token {
    public enum ID {
        Alias("<alias>"),
        Anchor("<anchor>"),
        BlockEnd("<block end>"),
        BlockEntry("-"),
        BlockMappingStart("<block mapping start>"),
        BlockSequenceStart("<block sequence start>"),
        Directive("<directive>"),
        DocumentEnd("<document end>"),
        DocumentStart("<document start>"),
        FlowEntry(","),
        FlowMappingEnd("}"),
        FlowMappingStart("{"),
        FlowSequenceEnd("]"),
        FlowSequenceStart("["),
        Key("?"),
        Scalar("<scalar>"),
        StreamEnd("<stream end>"),
        StreamStart("<stream start>"),
        Tag("<tag>"),
        Value(":"),
        Whitespace("<whitespace>"),
        Comment("#"),
        Error("<error>");

        private final String description;

        ID(String s) {
            description = s;
        }

        @Override
        public String toString() {
            return description;
        }
    }

    private final String startMark;
    private final String endMark;

    public Token(final String startMark, final String endMark) {
        if (startMark == null || endMark == null) {
            throw new IllegalArgumentException("Token requires marks.");
        }
        this.startMark = startMark;
        this.endMark = endMark;
    }

    /**
     * For error reporting.
     *
     * @return ID of this token
     * @see "class variable 'id' in PyYAML"
     */
    public abstract Token.ID getTokenId();
}
