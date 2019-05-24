package com.wildbeeslabs.jentle.algorithms.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Custom mapper utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@UtilityClass
public class CMapperUtils {

    /**
     * Default model mapper instance {@link ObjectMapper}
     */
    private static ObjectMapper mapper;

    /**
     * Model mapper property settings {@link ObjectMapper}
     * Default property matching strategy is set to Strict see {@link MatchingStrategies}
     * Custom mappings are added using {@link ModelMapper#addMappings(PropertyMap)}
     */
    static {
        mapper = new ObjectMapper();
        mapper.setDefaultMergeable(Boolean.TRUE);
        mapper.setLocale(Locale.getDefault());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        mapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
        mapper.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        mapper.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        mapper.enable(JsonGenerator.Feature.ESCAPE_NON_ASCII);

        mapper.disable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.UNWRAP_ROOT_VALUE);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);
        //mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    }

    public static Map<String, String> parseJson(final String json) {
        try {
            return mapper.readValue(json, new TypeReference<Map<String, String>>() {
            });
        } catch (IOException e) {
            log.error("ERROR: cannot parse string: {}, message: {}", json, e);
        }
        return null;
    }

    public static String prettyPrintJson(final Object json) {
        try {
            return mapper.writeValueAsString(json);
        } catch (IOException e) {
            log.error("ERROR: cannot parse string: {}, message: {}", json, e);
        }
        return null;
    }

    /**
     * Converts input entity by initial output class instance {@link Class}
     *
     * <p>Note: outClass object must have default constructor with no arguments</p>
     *
     * @param <T>      type of object to be converted from
     * @param <D>      type of objects in result list
     * @param source   - initial input object to be mapped {@code T}
     * @param outClass - initial input class to map by {@link Class}
     * @return mapped object of <code>outClass</code> type
     */
    public static <T, D> D map(final T source, final Class<? extends D> outClass) {
        return mapper.convertValue(source, outClass);
    }

    /**
     * Converts input object {@code source} to destination object {@code destination}
     *
     * @param <T>      type of object to be converted from
     * @param <D>      type of object to be converted to
     * @param source   - initial input source to be mapped from {@code T}
     * @param javaType - initial java type to be mapped to {@link JavaType}
     * @return mapped object with <code><D></code> type
     */
    public static <T, D> D map(final T source, final JavaType javaType) {
        return mapper.convertValue(source, javaType);
    }

    /**
     * Converts input collection of objects {@link Collection} by initial output class instance {@link Class}
     * <p>Note: outClass object must have default constructor with no arguments</p>
     *
     * @param <T>      type of object to be converted from
     * @param <D>      type of objects in result list
     * @param source   - initial input collection of objects {@code T} to be mapped {@link Collection}
     * @param outClass - initial input class to map by {@link Class}
     * @return list of mapped objects of <code>outClass</code> type
     */
    public static <T, D> List<? extends D> toList(final Collection<T> source, final Class<? extends D> outClass) {
        return Optional.ofNullable(source)
            .orElseGet(Collections::emptyList)
            .stream()
            .map(entity -> map(entity, outClass))
            .collect(Collectors.toList());
    }

    /**
     * Returns converted input object {@code source} to destination object {@code destination}
     *
     * @param <D>       type of object to be converted to
     * @param <V>       type of object to be mapped by
     * @param source    - initial input source to be mapped from {@code T}
     * @param outClass  - initial input class to convert to {@link Class}
     * @param viewClazz - initial input view class to converted by {@link Class}
     * @return mapped object with <code><D></code> type
     * @throws IOException
     */
    public static <D, V> D map(final String source, final Class<? extends D> outClass, final Class<? extends V> viewClazz) throws IOException {
        return mapper.readerWithView(viewClazz).forType(outClass).readValue(source);
    }

    /**
     * Returns converted input object {@code source} to destination object {@code destination}
     *
     * @param <D>      type of object to be converted to
     * @param source   - initial input source to be mapped from {@code T}
     * @param outClass - initial input class to convert to {@link Class}
     * @return mapped object with <code><D></code> type
     * @throws IOException
     */
    public static <D> D map(final String source, final Class<? extends D> outClass) throws IOException {
        return mapper.reader().forType(outClass).readValue(source);
    }

    /**
     * Returns converted input object {@code source} to destination object {@link List}
     *
     * @param <D>    type of element to be converted to
     * @param source - initial input source to be mapped from
     * @return mapped object {@link List} with <code><D></code> type
     * @throws IOException
     */
    public static <D> List<D> toList(final String source) throws IOException {
        return mapper.reader().forType(new TypeReference<List<D>>() {
        }).readValue(source);
    }

    /**
     * Returns converted input object {@code source} to destination object {@link Map}
     *
     * @param <K>    type of key element
     * @param <V>    type of value element
     * @param source - initial input source to be mapped from
     * @return mapped object {@link Map} with <code><K></code> key type, <code><V></code> value type
     * @throws IOException
     */
    public static <K, V> Map<K, V> toMap(final String source) throws IOException {
        return mapper.reader().forType(new TypeReference<Map<K, V>>() {
        }).readValue(source);
    }

    /**
     * Returns converted input object {@code source} to destination object {@link List}
     *
     * @param <T>    type of key element
     * @param source - initial input source to be mapped from
     * @param clazz  - initial input element {@link Class}
     * @return mapped object {@link Map} with <code><K></code> key type, <code><V></code> value type
     * @throws IOException
     */
    public static <T> List<T> toList(final String source, final Class<? extends T> clazz) throws IOException {
        return toCollection(source, List.class, clazz);
    }

    /**
     * Returns converted input object {@code source} to destination object {@link Set}
     *
     * @param <T>    type of key element
     * @param source - initial input source to be mapped from
     * @param clazz  - initial input element {@link Class}
     * @return mapped object {@link Map} with <code><K></code> key type, <code><V></code> value type
     * @throws IOException
     */
    public static <T> Set<T> toSet(final String source, final Class<? extends T> clazz) throws IOException {
        return toCollection(source, Set.class, clazz);
    }

    /**
     * Returns converted input object {@code source} to destination object {@link Set}
     *
     * @param <T>             type of key element
     * @param source          - initial input source to be mapped from
     * @param collectionClazz - initial input collection {@link Class}
     * @param elementClazz    - initial input element {@link Class}
     * @return mapped object {@link Map} with <code><K></code> key type, <code><V></code> value type
     * @throws IOException
     */
    public static <T, S extends Collection<T>> S toCollection(final String source, final Class<? extends S> collectionClazz, final Class<? extends T> elementClazz) throws IOException {
        final CollectionType collectionType = TypeFactory.defaultInstance().constructCollectionType(collectionClazz, elementClazz);
        return mapper.readerFor(collectionType).readValue(source);
    }

    /**
     * Returns converted input object {@code T} as string value
     *
     * @param <T>       type of object to be converted from
     * @param <V>       type of object to be mapped by
     * @param source    - initial input source to be mapped from {@code T}
     * @param viewClazz - initial input view class to converted by {@link Class}
     * @return string representation of input object
     * @throws JsonProcessingException
     */
    public static <T, V> String toJson(final T source, final Class<? extends V> viewClazz) throws JsonProcessingException {
        return mapper.writerWithView(viewClazz).writeValueAsString(source);
    }

    /**
     * Returns formatted input object {@code T} as string value
     *
     * @param <T>       type of object to be converted from
     * @param <V>       type of object to be mapped by
     * @param source    - initial input source to be mapped from {@code T}
     * @param viewClazz - initial input view class to converted by {@link Class}
     * @return string representation of input object
     * @throws JsonProcessingException
     */
    public static <T, V> String toFormatString(final T source, final Class<? extends V> viewClazz) throws JsonProcessingException {
        return mapper.writerWithView(viewClazz).withDefaultPrettyPrinter().writeValueAsString(source);
    }

    /**
     * Returns converted input object {@code T} as string value
     *
     * @param <T>    type of object to be converted from
     * @param source - initial input source to be mapped from {@code T}
     * @return string representation of input object
     * @throws JsonProcessingException
     */
    public static <T> String toJson(final T source) throws JsonProcessingException {
        return mapper.writeValueAsString(source);
    }

    /**
     * Returns formatted input object {@code T} as string value
     *
     * @param <T>    type of object to be converted from
     * @param source - initial input source to be mapped from {@code T}
     * @return string representation of input object
     * @throws JsonProcessingException
     */
    public static <T> String toFormatString(final T source) throws JsonProcessingException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(source);
    }
}
