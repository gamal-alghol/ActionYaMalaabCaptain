package com.malaab.ya.action.actionyamalaab.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonConverter {

    public static String objectToJson(Object obj) {
        String jsonStr = "";
        try {
            Gson gson = new GsonBuilder().serializeNulls().create();
            jsonStr = gson.toJson(obj);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return jsonStr;
    }

    public static <T> T jsonToObject(Class<T> clazz, String json) {
        T t = null;
        try {
            Gson gson = new GsonBuilder().serializeNulls().create();
            t = gson.fromJson(json, clazz);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return t;
    }
}