<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/home_page_product_data"/>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Nhà sách vui vẻ</title>
    </head>
    <body class="html front not-logged-in one-sidebar sidebar-second site-name-hidden browserChrome browserChrome6">
        <div id="page" class="container">
            <jsp:include page="HomePageHeader.jsp"/>
            <div id="columns">
                <div class="columns-inner clearfix">
                    <div id="content-column" style="">
                        <div class="content-inner">
                            <section id="main-content" role="main">
                                <div id="content">
                                    <div class="region region-content">
                                        <section id="block-views-sach-moi-block" class="block block-views ">
                                            <div class="block-inner clearfix">
                                                <h2 class="block-title">Sách mới</h2>
                                                <div class="block-content content">
                                                    <div class="view view-sach-moi view-id-sach_moi view-display-id-block view-dom-id-4d0e7c14c2df0334da04712bf6aed109">
                                                        <div class="view-content">
                                                            <div class="item-list-sm">  
                                                                <c:if test="${not empty books}">
                                                                    <ul>
                                                                        <c:forEach items="${books}" var="book">
                                                                            <li class="views-row">  
                                                                                <div class="views-field views-field-field-sach-anh-dai-dien">        
                                                                                    <div class="field-content">
                                                                                        <div style="width: 100px;height: 250px;">
                                                                                            <a href="/Book.jsp?id=${book.id}" target="_blank"><img src="${book.imageUrl}" style="max-width: 100%;max-height: 100%;margin: 0px;"></a>
                                                                                        </div>
                                                                                    </div>  
                                                                                </div>  
                                                                                <div class="views-field views-field-nothing">        
                                                                                    <span class="field-content">
                                                                                        <div>
                                                                                            <div style="margin-bottom: 20px;">
                                                                                                <font style="color: #00562f"><b>${book.title}</b></font>
                                                                                            </div>
                                                                                            <div>
                                                                                                <b>Tác giả:</b> ${book.author}
                                                                                            </div>
                                                                                            <div>
                                                                                                <b>Giá bìa:</b> ${book.price} VND
                                                                                            </div>
                                                                                        </div>
                                                                                    </span>  
                                                                                </div>
                                                                            </li>
                                                                        </c:forEach>
                                                                    </ul>
                                                                </c:if>
                                                            </div>
                                                            <h2 class="element-invisible">Trang</h2>
                                                            <div class="item-list">
                                                                <ul class="pager">
                                                                    <c:forEach begin="1" end="${pageQuantity}" varStatus="counter">
                                                                        <c:if test="${page==counter.index}">
                                                                            <li class="pager-current">${counter.index}</li>
                                                                        </c:if>
                                                                        <c:if test="${page!=counter.index}">
                                                                            <li class="pager-item">
                                                                                <a title="Đến trang ${counter.index}" href="
                                                                                   <c:url value="/Home.jsp">
                                                                                   <c:if test="${not empty param.search}">
                                                                                        <c:param name="search" value="${param.search}"/>
                                                                                    </c:if>
                                                                                    <c:param name="page" value="${counter.index}"/>
                                                                                    </c:url>">${counter.index}</a>
                                                                            </li>
                                                                        </c:if>
                                                                    </c:forEach>
                                                                </ul>
                                                            </div>  
                                                        </div>
                                                    </div>    
                                                </div>
                                        </section>
                                    </div>
                                </div>
                            </section>
                        </div>
                    </div>
                    <jsp:include page="SideBar.jsp"/>
                </div>
            </div>
            <jsp:include page="Footer.jsp"/>
        </div>
    </body>
</html>
