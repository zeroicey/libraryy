<%@ page import="com.acacia.libraryy.dao.UserDao" %>
<%@ page import="com.acacia.libraryy.model.User" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>悦读图书馆</title>
</head>
<body>
<table>
    <tr>
        <th>用户名</th>
        <th>密码</th>
        <th>邮箱</th>
    </tr>
    <%
        UserDao userDao = new UserDao();
        List<User> userList = userDao.getAllUsers();
        for (User user : userList) {
    %>
    <tr>
        <td><%= user.getUsername() %></td>
        <td><%= user.getPassword() %></td>
        <td><%= user.getEmail() %></td>
    </tr>
    <% } %>
</table>
</body>
</html>