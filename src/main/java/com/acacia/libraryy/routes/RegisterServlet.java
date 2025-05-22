package com.acacia.libraryy.routes;

import com.acacia.libraryy.dao.UserDao;
import com.acacia.libraryy.model.User;
import com.acacia.libraryy.utils.ResponseUtil;
import com.alibaba.fastjson2.JSON;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/registerServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        if (username != null && password != null && email != null) {
            User user = new User(username, password, email);
            UserDao userDao = new UserDao();
            boolean ret = userDao.insertUser(user);

            if (ret) {
                out.print(JSON.toJSONString(ResponseUtil.generateResponse(true, "注册成功! 赶紧去登录吧~")));
            } else {
                out.print(JSON.toJSONString(ResponseUtil.generateResponse(false, "字段填写有误!")));
            }
        } else {
            out.print(JSON.toJSONString(ResponseUtil.generateResponse(false, "请填写完整信息！")));
        }
    }
}