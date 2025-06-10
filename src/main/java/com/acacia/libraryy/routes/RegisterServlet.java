package com.acacia.libraryy.routes;

import com.acacia.libraryy.dao.UserDao;
import com.acacia.libraryy.model.User;
import com.acacia.libraryy.utils.Responder;
import com.alibaba.fastjson2.JSON;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@WebServlet("/api/auth/register")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder jsonBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
        }
        String jsonString = jsonBuilder.toString();
        JSONObject jsonObject = JSON.parseObject(jsonString);

        // 提取 JSON 中的数据
        String username = jsonObject.getString("username");
        String email = jsonObject.getString("email");
        String password = jsonObject.getString("password");

        System.out.println(username);
        System.out.println(email);
        System.out.println(password);

        if (username != null && password != null && email != null) {
            UserDao userDao = new UserDao();
            boolean ret = userDao.insertUser(username, password, email);
            if (ret) {
                Responder.success("Register Successfully").build(response);
            } else {
                Responder.fail("User has been registered").build(response);
            }
        } else {
            Responder.fail("Invalid request body").build(response);
        }
    }
}