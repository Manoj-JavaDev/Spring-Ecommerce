<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
<link rel="stylesheet" type="text/css" href="css/register1.css">
</head>
<body>
    <div class="container">
        <h2>Login</h2>

        <form action="doLogin" method="post">

            <label>Email Address</label>
            <input type="email" name="email" required />

            <label>Password</label>
            <input type="password" name="password" required />

            <button type="submit">Login</button>

        </form>

        <div class="footer-link">
            No account? <a href="register">Register</a>
        </div>

        <c:if test="${param.error != null}">
            <p style="color:red;">Invalid email or password</p>
        </c:if>

        <c:if test="${param.logout != null}">
            <p style="color:green;">You have been logged out successfully</p>
        </c:if>

    </div>
</body>
</html>