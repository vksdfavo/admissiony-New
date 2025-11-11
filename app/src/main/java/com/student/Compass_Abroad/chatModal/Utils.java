package com.student.Compass_Abroad.chatModal;

import com.google.gson.Gson;

import java.lang.reflect.Type;


public class Utils {
    public static <T> T toModel(String data, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(data, type);
    }
}
