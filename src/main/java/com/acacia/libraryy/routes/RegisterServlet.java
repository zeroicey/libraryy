package com.acacia.libraryy.routes;

import java.io.*;

import com.acacia.libraryy.dao.UserDao;
import com.acacia.libraryy.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/registerServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        String msg = "";

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        if (username != null && password != null && email != null) {
            User user = new User(username, password, email);
            UserDao userDao = new UserDao();
            boolean ret = userDao.insertUser(user);
            if (ret) {
                msg = "注册成功! 赶紧去登录吧~";
                out.println("<script>alert('" + msg + "');window.location.href='login.jsp';</script>");
            }
            else {
                msg = "字段填写有误!";
                out.println("<script>alert('" + msg + "');window.location.href='register.jsp';</script>");
            }
        } else {
            msg = "请填写完整信息！";
            out.println("<script>alert('" + msg + "');window.location.href='register.jsp';</script>");
        }

    }
}
