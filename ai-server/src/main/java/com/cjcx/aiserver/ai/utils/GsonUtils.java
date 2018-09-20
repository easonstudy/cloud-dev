package com.cjcx.aiserver.ai.utils;

import com.google.gson.Gson;

public class GsonUtils {


    private static Gson gson = new Gson();

    public static Gson getInstance() {
        return gson;
    }

    private GsonUtils() {
    }


}
