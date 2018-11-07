<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page session="false" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/product_data"/>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Kho sách</title>
        <script type="text/javascript" src="/js/Common.js"></script>
        <script type="text/javascript" src="/js/BookController.js"></script>
        <script type="text/javascript" src="/js/BookCache.js"></script>
        <script>
            function filterByType() {
                var url = "http://" + window.location.hostname + ":" + window.location.port + "/admin/Home.jsp?type=";
                var element = document.getElementById("type");
                url += element.value;
                var search = document.getElementById("searchTxt");
                if (search !== null && search.value.trim() !== '') {
                    url += "&search=" + search.value;
                }
                window.location.href = url;
                return false;
            }
        </script>
        <c:if test='${not empty xmlData}'>
            <script id="xmlData" type="text/xmldata">${xmlData}</script>
        </c:if>
    </head>
    <body class="html front not-logged-in one-sidebar sidebar-second site-name-hidden browserChrome browserChrome6">
        <jsp:include page="BookDetail.jsp"/>
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
                                            <div class="block-content content">
                                                <form action="Home.jsp" style="margin-left: 500px;">
                                                    <div>
                                                        Lọc:
                                                        <select name="type" id="type">
                                                            <option value="active" <c:if test="${param.type=='active'}">selected=""</c:if>>Đang kinh doanh</option>
                                                            <option value="out-of-stock" <c:if test="${param.type=='out-of-stock'}">selected=""</c:if>>Hết hàng</option>
                                                            <option value="disable" <c:if test="${param.type=='disable'}">selected=""</c:if>>Ngừng kinh doanh</option>
                                                            </select>
                                                            <button onclick="return filterByType()">Lọc</button>
                                                        </div>
                                                        <input type="text" name="search" value="${param.search}" id="searchTxt"/>
                                                    <input type="submit" value="Tìm kiếm"/>
                                                </form>
                                            </div>
                                            <div class="block-inner clearfix">
                                                <h2 class="block-title">Danh mục sản phẩm mới</h2>
                                                <div class="block-content content">
                                                    <div class="view view-sach-moi view-id-sach_moi view-display-id-block view-dom-id-4d0e7c14c2df0334da04712bf6aed109">
                                                        <div class="view-content">
                                                            <div class="item-list-sm">  
                                                                <c:choose>
                                                                    <c:when test="${not empty books and fn:length(books)>0}">
                                                                        <ul>
                                                                            <c:forEach items="${books}" var="book">
                                                                                <li class="views-row book_item">
                                                                                    <div class="book_actions_container">
                                                                                        <c:choose>
                                                                                            <c:when test="${book.status==1}">
                                                                                                <button class="book_action_button book_action_button_green">Thêm sản phẩm</button>
                                                                                                <button class="book_action_button book_action_button_orange" 
                                                                                                        onclick="bookController.setOutOfStockBook('${book.id}')">Hết hàng</button>
                                                                                                <button class="book_action_button book_action_button_red" 
                                                                                                        onclick="bookController.setDisableBook('${book.id}')">Ngừng kinh doanh</button>
                                                                                            </c:when>
                                                                                            <c:when test="${book.status==0 || book.status==2}">
                                                                                                <button class="book_action_button book_action_button_green" 
                                                                                                        onclick="bookController.setActiveBook('${book.id}')">Kinh doanh sản phẩm</button>
                                                                                                <c:if test="${book.status==2}">
                                                                                                    <button class="book_action_button book_action_button_red" 
                                                                                                            onclick="bookController.setDisableBook('${book.id}')">Ngừng kinh doanh</button>
                                                                                                </c:if>
                                                                                            </c:when>
                                                                                        </c:choose>
                                                                                    </div>
                                                                                    <div class="views-field views-field-field-sach-anh-dai-dien">        
                                                                                        <div class="field-content">
                                                                                            <div style="width: 100px;height: 250px;">
                                                                                                <img src="${book.imageUrl}" style="max-width: 100%;max-height: 100%;margin: 0px;"
                                                                                                     class="book_detail_trigger" onclick="bookDetailFrame.showBookDetailModel('${book.id}');">
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
                                                                                                <div>
                                                                                                    <b>Số lượng:</b> ${book.quantity}
                                                                                                </div>
                                                                                                <div>
                                                                                                    <b>Trạng thái:</b> 
                                                                                                    <c:choose>
                                                                                                        <c:when test="${book.status==1}">
                                                                                                            <font style="color: green"><b>Kinh doanh</b></font>
                                                                                                        </c:when>
                                                                                                        <c:when test="${book.status==2}">
                                                                                                            <font style="color: orange"><b>Hết hàng</b></font>
                                                                                                        </c:when>
                                                                                                        <c:otherwise>
                                                                                                            <font style="color: red"><b>Ngừng kinh doanh</b></font>
                                                                                                        </c:otherwise>
                                                                                                    </c:choose>
                                                                                                </div>
                                                                                            </div>
                                                                                        </span>  
                                                                                    </div>
                                                                                </li>
                                                                            </c:forEach>
                                                                        </ul>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <h3>Không tìm thấy kết quả phù hợp</h3>
                                                                    </c:otherwise>
                                                                </c:choose>
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
                                                                                   <c:url value="/admin/Home.jsp">
                                                                                       <c:if test="${not empty param.search}">
                                                                                           <c:param name="search" value="${param.search}"/>
                                                                                       </c:if>
                                                                                       <c:if test="${not empty param.type}">
                                                                                           <c:param name="type" value="${param.type}"/>
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
