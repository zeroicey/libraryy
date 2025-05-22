package com.acacia.libraryy.utils;

import com.alibaba.fastjson2.JSONObject;

public class ResponseUtil {

    public static JSONObject generateResponse(boolean success, String message, Object data) {
        JSONObject response = new JSONObject();
        response.put("success", success);
        response.put("message", message);
        response.put("data", data);
        return response;
    }

    public static JSONObject generateResponse(boolean success, String message) {
        return generateResponse(success, message, null);
    }

    public static JSONObject generateResponse(boolean success) {
        return generateResponse(success, "", null);
    }
}