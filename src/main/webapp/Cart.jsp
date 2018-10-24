<%-- 
    Document   : Cart
    Created on : Oct 24, 2018, 5:01:17 PM
    Author     : ADMIN
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Giỏ hàng</title>
    </head>
    <body class="html front not-logged-in one-sidebar sidebar-second site-name-hidden browserChrome browserChrome6">
        <div id="page" class="container">
            <c:set var="no_banner" value="true" scope="request"/>
            <jsp:include page="HomePageHeader.jsp"/>
            <div id="columns">
                <div class="columns-inner clearfix">
                    <div id="content-column" style="">
                        <div class="content-inner">

                        </div>
                    </div>
                    <jsp:include page="SideBar.jsp"/>
                </div>
            </div>
            <jsp:include page="Footer.jsp"/>
        </div>
    </body>
</html>
