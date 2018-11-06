<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page session="false" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Giỏ hàng</title>
        <script>
            function redirectToLogin() {
                var url = "http://" + window.location.hostname + ":" + window.location.port + "/Login.jsp?redirectPage=Cart.jsp";
                window.location.href = url;
            }
        </script>
    </head>
    <body class="html front not-logged-in one-sidebar sidebar-second site-name-hidden browserChrome browserChrome6">
        <div id="page" class="container">
            <c:set var="no_banner" value="true" scope="request"/>
            <jsp:include page="HomePageHeader.jsp"/>
            <div id="columns">
                <div class="columns-inner clearfix">
                    <div id="content-column" style="">
                        <div class="content-inner">
                            <table>
                                <thead>
                                    <tr>
                                        <th>Mã sản phẩm</th>
                                        <th>Tên sách</th>
                                        <th>Tác giả</th>
                                        <th>Giá</th>
                                        <th>Số lượng</th>
                                        <th>Tổng</th>
                                        <th></th>
                                    </tr>
                                </thead>
                                <tbody id="cart_container">

                                </tbody>
                            </table>
                            <%
                                HttpSession httpSession = request.getSession(false);
                                if (httpSession != null && httpSession.getAttribute("USER") != null) {
                            %>
                            <button style="float: right;" onclick="cartView.checkout();">Đặt hàng</button>
                            <%
                            } else {
                            %>
                            <button style="float: right;" onclick="redirectToLogin();">Đặt hàng</button>
                            <%
                                }
                            %>
                        </div>
                    </div>
                    <jsp:include page="SideBar.jsp"/>
                </div>
            </div>
            <jsp:include page="Footer.jsp"/>
        </div>
    </body>
</html>
