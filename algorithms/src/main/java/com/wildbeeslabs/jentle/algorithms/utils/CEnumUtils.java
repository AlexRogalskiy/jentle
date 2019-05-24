package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Objects;

/**
 * Custom enum utilities implementation
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 */
@UtilityClass
public class CEnumUtils {

    /**
     * Returns an enum value for the given id.
     *
     * @param e    enum class
     * @param name id
     * @return enum value or null if the passed id is null
     * @throws IllegalArgumentException if there are no enum values with the given id
     */
    public static <T extends Enum<T>> T fromName(final Class<T> e, final String name) {
        if (Objects.isNull(name)) {
            return null;
        }
        return Arrays.stream(e.getEnumConstants())
            .filter(enumConstant -> enumConstant.name().equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(String.format("Can't parse {%s} from name {%s}", e.getSimpleName(), name)));
    }

    /**
     * Returns an enum value for the given id.
     *
     * @param e            enum class
     * @param id           id
     * @param defaultValue the value to return if null is passed as id
     * @return enum value
     * @throws IllegalArgumentException if there are no enum values with the given id
     */
    public static <T extends Enum<T>> T fromName(final Class<T> e, final String name, final T defaultValue) {
        if (Objects.isNull(name)) {
            return defaultValue;
        }
        return Arrays.stream(e.getEnumConstants())
            .filter(enumConstant -> enumConstant.name().equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(String.format("Can't parse {%s} from name {%s}", e.getSimpleName(), name)));
    }

    /**
     * Returns an enum value for the given id, or the default value if null is passed or there are no enum values with
     * the given id.
     *
     * @param e            enum class
     * @param id           id
     * @param defaultValue the value to return if null is passed as id or if there are no enum values with the given id
     * @return enum value
     */
    public static <T extends Enum<T>> T fromNameOrDefault(final Class<T> e, final String name, final T defaultValue) {
        if (Objects.isNull(name)) {
            return defaultValue;
        }
        return Arrays.stream(e.getEnumConstants())
            .filter(enumConstant -> enumConstant.name().equalsIgnoreCase(name))
            .findFirst()
            .orElse(defaultValue);
    }
}
