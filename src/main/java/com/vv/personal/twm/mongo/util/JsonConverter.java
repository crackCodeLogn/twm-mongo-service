package com.vv.personal.twm.mongo.util;

import com.google.gson.Gson;

/**
 * @author Vivek
 * @since 16/11/20
 */
public class JsonConverter {

    private static final Gson GSON = new Gson();

    public static <T> String convertToJson(T object) {
        return GSON.toJson(object);
    }
}
