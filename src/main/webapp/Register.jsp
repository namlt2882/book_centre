<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Đăng kí thành viên của Nhà sách vui vẻ</title>
        <script type="text/javascript" src="/js/Common.js"></script>
        <script type="text/javascript" src="/js/Register.js"></script>
    </head>
    <body>
        <div style="width: 60%;margin: 50px 10% 0% 30%;">
            <img alt="" src="/img/sachvuive.gif" style="width: 70px;">
            <h1>Nhà sách vui vẻ</h1>
            <h3>Đăng kí thành viên</h3>
            <form action="Register" method="POST" onsubmit="return register();">
                <table>
                    <tr>
                        <td>Username:</td>
                        <td><input type="text" name="username" id="register_username" required="true" maxlength="30" minlength="5"></td>
                    </tr>
                    <tr>
                        <td>Mật khẩu:</td>
                        <td><input type="password" name="password" id="register_password" required="true" maxlength="50" minlength="5"></td>
                    </tr>
                    <tr>
                        <td>Nhập lại:</td>
                        <td><input type="password" name="confirm" id="register_confirm" required="true"></td>
                    </tr>
                    <tr>
                        <td>Tên:</td>
                        <td><input type="text" name="name" id="register_name" required="true" maxlength="200"></td>
                    </tr>
                    <tr>
                        <td>Địa chỉ giao hàng:</td>
                        <td><input type="text" name="address" id="register_address" required="true" maxlength="200"></td>
                    </tr>
                    <tr>
                        <td>Số điện thoại:</td>
                        <td><input type="text" name="phone" id="register_phone" required="true"></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td><input type="submit" value="Đăng kí ngay!"></td>
                    </tr>
                    <tr>
                        <td>Đã có tài khoản? <a id="register_redirectUrl" href="<c:url value="/Login.jsp">
                                                    <c:if test="${not empty param.redirectPage}">
                                                        <c:param name="redirectPage" value="${param.redirectPage}" />
                                                    </c:if>
                                                </c:url>">Đăng nhập</a></td>
                        <td></td>
                    </tr>
                </table>
            </form>
        </div>
    </body>
</html>
