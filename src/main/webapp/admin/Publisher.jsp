<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/crawl"/>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dữ liệu nhà xuất bản</title>
    </head>
    <body class="html front not-logged-in one-sidebar sidebar-second site-name-hidden browserChrome browserChrome6">
        <div id="page" class="container">
            <jsp:include page="AdminPageHeader.jsp"/>
            <div id="columns">
                <div class="columns-inner clearfix">
                    <div id="content-column" style="">
                        <div class="content-inner">
                            <section id="main-content" role="main">
                                <div id="content">
                                    <div class="region region-content">
                                        <section id="block-views-sach-moi-block" class="block block-views ">
                                            <div class="block-inner clearfix">
                                                <div class="block-content content">
                                                    <form action="Publisher.jsp" style="margin-left: 500px;">
                                                        <div>
                                                            Chọn nhà xuất bản:
                                                            <select name="publisher">
                                                                <option value="nxb-nhanam">Nhã Nam</option>
                                                                <option value="nxb-tre">Trẻ</option>
                                                            </select>
                                                            <button>Sản phẩm mới</button>
                                                        </div>
                                                        <input type="text" name="search"/>
                                                        <button>Tìm kiếm</button>
                                                    </form>
                                                </div>
                                                <c:if test="${empty param.search}">
                                                    <h2 class="block-title">Danh mục sản phẩm mới nhất 
                                                        của nxb 
                                                        <c:if test="${empty param.publisher or param.publisher=='nxb-nhanam'}">Nhã Nam</c:if>
                                                        <c:if test="${param.publisher=='nxb-tre'}">Trẻ</c:if>
                                                        </h2>
                                                </c:if>
                                                <c:if test="${not empty param.search}">
                                                    <h2 class="block-title">Kết quả tìm kiếm cho "${param.search}" 
                                                        của nxb 
                                                        <c:if test="${param.publisher=='nxb-nhanam'}">Nhã Nam</c:if>
                                                        <c:if test="${param.publisher=='nxb-tre'}">Trẻ</c:if>
                                                        </h2>
                                                </c:if>
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
                                                                                        <img class="hover-preview-imgpreview hover-preview hover-preview-imgpreview-processed" 
                                                                                             src="${book.imageUrl}">

                                                                                    </div>  
                                                                                </div>  
                                                                                <div class="views-field views-field-nothing">        <span class="field-content"><div>
                                                                                            <div><b><a href="/sach/101-bo-phim-viet-nam-hay-nhat">${book.title}</a></b></div>
                                                                                            <div><b>Tác giả:</b> <a href="/tac-gia/le-hong-lam">${book.author}</a></div>
                                                                                            <div><b>Giá bìa:</b> ${book.price} VND</div>
                                                                                        </div>
                                                                                    </span>  
                                                                                </div>
                                                                            </li>
                                                                        </c:forEach>
                                                                    </ul>
                                                                </c:if>
                                                            </div>    
                                                        </div>
                                                    </div>    
                                                </div>
                                                <div class="block-content content">
                                                    <input type="submit" value="Trang kế tiếp"/>
                                                </div>  
                                            </div>
                                        </section>
                                    </div>
                                </div>
                            </section>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
