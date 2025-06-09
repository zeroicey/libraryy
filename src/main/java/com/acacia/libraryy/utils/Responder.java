package com.acacia.libraryy.utils;

import com.alibaba.fastjson2.JSONObject;

public class Responder {
    private Boolean status;
    private String message;
    private Integer statusCode;
    private Object data = null;


    public Responder(boolean status, String message, Integer statusCode, Object data) {
        this.message = message;
        this.data = data;
        this.statusCode = statusCode;
        this.status = status;
//        JSONObject response = new JSONObject();
//        response.put("message", message);
//        response.put("data", data);
//        response.put("statusCode", statusCode);
//        response.put("status", status);
//        return response;
    }

    public Responder(boolean status, String message, Integer statusCode) {
        this.message = message;
        this.data = null;
        this.statusCode = statusCode;
        this.status = status;
    }

    public static Responder success(String message) {
        return new Responder(true, message, 200);
    }

    public static Responder fail(String message) {
        return new Responder(true, message, 400);
    }
}