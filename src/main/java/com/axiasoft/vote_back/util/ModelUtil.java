package com.axiasoft.vote_back.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class ModelUtil {
    public static <T> T fromMap(Map map, Class<T> classType) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(objectMapper.writeValueAsString(map), classType);
    }

    public static <T> Map<String, Object> toMap(T classType) {
        // ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        Map result = objectMapper.convertValue(classType, Map.class);
        return result;
    }
}