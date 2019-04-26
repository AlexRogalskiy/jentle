package com.wildbeeslabs.jentle.algorithms.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.Map;

public class CMapperUtils {

    private static ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public static Map<String, String> parseJson(final String json) {
        try {
            return mapper.readValue(json, new TypeReference<Map<String, String>>() {
            });
        } catch (IOException e) {
            return null;
        }
    }

    public static String prettyPrintJson(final Object json) {
        try {
            return mapper.writeValueAsString(json);
        } catch (IOException e) {
            return null;
        }
    }
}
