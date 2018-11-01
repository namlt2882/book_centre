<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
    </head>
    <body>
        <div style="width: 60%;margin: 50px 10% 0% 30%;">
            <img alt="" src="/img/sachvuive.gif" style="width: 70px;">
            <h1>Nhà sách vui vẻ</h1>
            <h3>Đăng nhập vào hệ thống</h3>
            <form action="Login" method="POST">
                <c:if test="${not empty param.redirectPage}">
                    <input type="hidden" name="redirectPage" value="${param.redirectPage}">
                </c:if>
                <table>
                    <tr>
                        <td>Username:</td>
                        <td><input type="text" name="username"></td>
                    </tr>
                    <tr>
                        <td>Mật khẩu:</td>
                        <td><input type="password" name="password"></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td><input type="submit" value="Đăng nhập ngay!"></td>
                    </tr>
                </table>
            </form>
        </div>
    </body>
</html>
