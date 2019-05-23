package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum ResourceType {
    /**
     * Represents any ResourceType which this client cannot understand,
     * perhaps because this client is too old.
     */
    UNKNOWN((byte) 0),

    /**
     * In a filter, matches any ResourceType.
     */
    ANY((byte) 1),

    /**
     * A Kafka topic.
     */
    TOPIC((byte) 2),

    /**
     * A consumer group.
     */
    GROUP((byte) 3),

    /**
     * The cluster as a whole.
     */
    CLUSTER((byte) 4),

    /**
     * A transactional ID.
     */
    TRANSACTIONAL_ID((byte) 5),

    /**
     * A token ID.
     */
    DELEGATION_TOKEN((byte) 6);

    private final static Map<Byte, ResourceType> CODE_TO_VALUE = new HashMap<>();

    static {
        CODE_TO_VALUE.putAll(Arrays.stream(ResourceType.values()).collect(Collectors.toMap(ResourceType::getCode, Function.identity())));
    }

    /**
     * Parse the given string as an ACL resource type.
     *
     * @param name The string to parse.
     * @return The ResourceType, or UNKNOWN if the string could not be matched.
     */
    public static ResourceType fromName(final String name) {
        return Arrays.stream(values())
            .filter(type -> type.name().equalsIgnoreCase(name))
            .findFirst()
            .orElse(UNKNOWN);
    }

    /**
     * Return the ResourceType with the provided code or `ResourceType.UNKNOWN` if one cannot be found.
     */
    public static ResourceType fromCode(final byte code) {
        final ResourceType resourceType = CODE_TO_VALUE.get(code);
        if (Objects.isNull(resourceType)) {
            return UNKNOWN;
        }
        return resourceType;
    }

    private final byte code;

    /**
     * Return whether this resource type is UNKNOWN.
     */
    public boolean isUnknown() {
        return Objects.equals(this, UNKNOWN);
    }
}
