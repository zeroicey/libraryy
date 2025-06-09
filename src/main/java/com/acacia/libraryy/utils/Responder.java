package com.acacia.libraryy.utils;

import java.io.IOException;
import java.io.PrintWriter;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletResponse;

public class Responder {
    private Boolean status;
    private final String message;
    private Integer statusCode;
    private Object data = null;

    public Responder(boolean status, String message, Integer statusCode, Object data) {
        this.message = message;
        this.data = data;
        this.statusCode = statusCode;
        this.status = status;
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

    public static Responder success(Object data) {
        return new Responder(true, "Request Successfully", 200, data);
    }

    public static Responder fail(String message) {
        return new Responder(false, message, 400);
    }

    public Responder setData(Object data) {
        this.data = data;
        return this;
    }

    public Responder setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public Responder setStatus(Boolean status) {
        this.status = status;
        return this;
    }

    public void build(HttpServletResponse response)  {
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        response.setStatus(this.statusCode);
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", this.message);
        jsonObject.put("status", this.status);
        jsonObject.put("data", this.data);

        jsonObject.toJSONString();
        out.print(jsonObject);
    }
}