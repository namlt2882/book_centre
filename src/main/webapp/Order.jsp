<%@page import="namlt.xml.asm.prj.model.OrderDetail"%>
<%@page import="namlt.xml.asm.prj.model.Order"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/my_order"/>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Đơn hàng của tôi</title>
        <script src="/js/OrderController.js"></script>
        <c:if test="${not empty orderXmlData}">
            <script id="orderXmlData">${orderXmlData}</script>
        </c:if>
    </head>
    <body class="html front not-logged-in one-sidebar sidebar-second site-name-hidden browserChrome browserChrome6">
        <div id="page" class="container">
            <c:set var="no_banner" value="true" scope="request"/>
            <jsp:include page="HomePageHeader.jsp"/>
            <div id="columns">
                <div class="columns-inner clearfix">
                    <div id="content-column" style="">
                        <div class="content-inner">
                            <c:choose>
                                <c:when test="${not empty orders and fn:length(orders)>0}">
                                    <c:forEach items="${orders}" var="order">
                                        <div style="padding: 30px;border: #000 2px dotted;margin-top: 30px;">
                                            <h3>Đơn hàng gồm ${fn:length(order.orderDetails)} sản phẩm vào lúc 
                                                <fmt:formatDate pattern="hh:mm 'ngày' dd 'tháng' MM 'năm' YYYY" value="${order.insertDate}"/>
                                            </h3>
                                            <h4>Trạng thái: 
                                                <c:choose>
                                                    <c:when test="${order.status==1}">
                                                        <font color="green">Đã tiếp nhận</font>
                                                    </c:when>
                                                    <c:when test="${order.status==2}">
                                                        <font color="green">Đang vận chuyển</font>
                                                    </c:when>
                                                    <c:when test="${order.status==2}">
                                                        <font color="green">Đã giao hàng</font>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <font color="red">Hủy</font>
                                                    </c:otherwise>
                                                </c:choose>
                                            </h4>
                                            <table style="background-color: white;">
                                                <tr>
                                                    <th>Mã sản phẩm</th>
                                                    <th>Tên sản phẩm</th>
                                                    <th>Giá</th>
                                                    <th>Số lượng</th>
                                                </tr>
                                                <%
                                                    int sum = 0;
                                                %>
                                                <c:forEach items="${order.orderDetails}" var="orderDetail">
                                                    <%
                                                        OrderDetail orderDetail = (OrderDetail) pageContext.getAttribute("orderDetail");
                                                        sum += orderDetail.getItemPrice() * orderDetail.getQuantity();
                                                    %>
                                                    <tr>
                                                        <td>${orderDetail.bookId}</td>
                                                        <td>${orderDetail.title}</td>
                                                        <td>${orderDetail.itemPrice}</td>
                                                        <td>${orderDetail.quantity}</td>
                                                    </tr>
                                                </c:forEach>
                                                <tr>
                                                    <td colspan="3" style="text-align: right;"><b>Tổng tiền</b></td>
                                                    <td><%=sum%></td>
                                                </tr>
                                            </table>
                                            <c:if test="${order.status==1}">
                                                <button style="float: right" 
                                                        onclick="orderController.cancelOrder('${order.id}')">Hủy đơn hàng</button>
                                            </c:if>
                                        </div>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <div style="padding: 30px;border: #000 2px dotted;margin-top: 30px;">
                                        <h3>Bạn chưa có đơn hàng nào :))</h3>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <jsp:include page="SideBar.jsp"/>
                </div>
            </div>
            <jsp:include page="Footer.jsp"/>
        </div>
    </body>
</html>
