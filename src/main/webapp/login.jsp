<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="header.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>用户登录</title>
</head>
<body class="bg-gradient-to-r from-blue-900 via-red-900 to-yellow-600 min-h-screen relative">
<!-- 浮动返回主页按钮 -->
<a href="index.jsp" class="absolute top-8 left-8 bg-white/90 backdrop-blur-sm rounded-full p-3 shadow-lg flex items-center gap-2">
    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-blue-600" viewBox="0 0 20 20" fill="currentColor">
        <path fill-rule="evenodd" d="M9.707 16.707a1 1 0 01-1.414 0l-6-6a1 1 0 010-1.414l6-6a1 1 0 111.414 1.414L5.414 9H17a1 1 0 110 2H5.414l4.293 4.293a1 1 0 010 1.414z" clip-rule="evenodd" />
    </svg>
    返回主页
</a>

<div class="bg-white/90 backdrop-blur-sm shadow-xl rounded-xl p-10 w-96 absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2">
    <div class="text-center mb-8">
        <h1 class="text-3xl text-blue-900 font-bold mb-2">账户登录</h1>
        <p class="text-gray-500">欢迎回来，请登录您的账户</p>
    </div>

    <form action="login" method="post" class="space-y-6">
        <div>
            <label for="username" class="block text-sm text-gray-700 mb-2">用户名 / 邮箱</label>
            <input type="text" id="username" name="username" placeholder="请输入用户名或邮箱" class="w-full px-4 py-3 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none">
        </div>

        <div>
            <label for="password" class="block text-sm text-gray-700 mb-2">密码</label>
            <input type="password" id="password" name="password" placeholder="请输入密码" class="w-full px-4 py-3 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none">
        </div>

        <button type="submit" class="w-full bg-gradient-to-r from-blue-700 to-blue-900 text-white py-3 rounded-md font-semibold">登录</button>
    </form>

    <div class="text-center mt-6 text-sm text-gray-500">
        <p>还没有账户？ <a href="register.jsp" class="text-blue-600 hover:text-blue-800">立即注册</a></p>
    </div>
</div>
</body>
</html>
<%@ include file="footer.jsp"%>