package com.acacia.libraryy.routes;

import com.acacia.libraryy.dao.UserDao;
import com.acacia.libraryy.model.User;
import com.acacia.libraryy.utils.JwtUtil;
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
import java.io.PrintWriter;

@WebServlet("/api/auth/login")
public class LoginServlet extends HttpServlet {
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
        String password = jsonObject.getString("password");

        System.out.println(username);

        PrintWriter out = response.getWriter();

        // select user by username
        UserDao userDao = new UserDao();
        User user = userDao.getUserByUsername(username);

        if (user == null) {
            Responder.fail("User not found").build(response);
            return;
        }

        if (!user.getPassword().equals(password)) {
            Responder.fail("Password incorrect").build(response);
            return;
        }

        String token = JwtUtil.genToken(user.getId());

        // 创建登录响应数据，包含用户信息和token
        JSONObject loginData = new JSONObject();
        loginData.put("token", token);
        loginData.put("user", new JSONObject() {
            {
                put("id", user.getId());
                put("username", user.getUsername());
                put("email", user.getEmail());
                // 不返回密码信息
            }
        });

        Responder.success("Login successful").setData(loginData).build(response);
    }
}