<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
        <script type="text/javascript" src="/js/Common.js"></script>
        <script type="text/javascript" src="/js/Login.js"></script>
    </head>
    <body>
        <div style="width: 60%;margin: 50px 10% 0% 30%;">
            <img alt="" src="/img/sachvuive.gif" style="width: 70px;">
            <h1>Nhà sách vui vẻ</h1>
            <h3>Đăng nhập vào hệ thống</h3>
            <form action="Login" method="POST" onsubmit="return login();">
                <c:if test="${not empty param.redirectPage}">
                    <input type="hidden" name="redirectPage" value="${param.redirectPage}" id="login_redirectPage">
                </c:if>
                <table>
                    <tr>
                        <td>Username:</td>
                        <td><input type="text" name="username" id="login_username" required="true"></td>
                    </tr>
                    <tr>
                        <td>Mật khẩu:</td>
                        <td><input type="password" name="password" id="login_password" required="true"></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td><input type="submit" value="Đăng nhập ngay!"></td>
                    </tr>
                    <tr>
                        <td><a href="<c:url value="/Register.jsp">
                                   <c:if test="${not empty param.redirectPage}">
                                       <c:param name="redirectPage" value="${param.redirectPage}" />
                                   </c:if>
                               </c:url>">Đăng kí nhanh</a></td>
                        <td></td>
                    </tr>
                </table>
            </form>
        </div>
    </body>
</html>
