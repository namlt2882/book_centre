<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page session="false" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/book"/>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Nhà sách Vui Vẻ</title>
    </head>
    <body class="html front not-logged-in one-sidebar sidebar-second site-name-hidden browserChrome browserChrome6">
        <div id="page" class="container">
            <jsp:include page="HomePageHeader.jsp"/>
            <div id="columns">
                <div class="columns-inner clearfix">
                    <div id="content-column" style="">
                        <div class="content-inner">
                            <section id="main-content" role="main">
                                <header>
                                    <h1 id="page-title">Chào mừng đến với Nhã Nam</h1>
                                </header>
                                <div id="content">
                                    <div class="region region-content">
                                        <section id="block-views-sach-moi-block" class="block block-views ">
                                            <div class="block-inner clearfix">
                                                <h2 class="block-title">${book.title}</h2>
                                                <div class="block-content content">
                                                    <article id="article-4327" class="article article-type-sach clearfix" role="article">
                                                        <div class="article-content">
                                                            <div class="field field-name-field-sach-anh-dai-dien field-type-image field-label-hidden">
                                                                <div class="field-items">
                                                                    <div class="field-item even">
                                                                        <img src="${book.imageUrl}" width="157" height="214" alt="">
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <fieldset class="field-group-fieldset  form-wrapper" id="node_sach_full_group_thong_tin_sach">
                                                                <div class="fieldset-wrapper">
                                                                    <section class="field field-type-text field-label-inline clearfix">
                                                                        <h2 class="field-label">Tác giả:&nbsp;</h2>
                                                                        <div class="field-items">
                                                                            <div class="field-item even">${book.author}</div>

                                                                        </div>
                                                                    </section>
                                                                    <c:if test="${book.translator!=null and book.translator!=''}">
                                                                        <section class="field field-type-text field-label-inline clearfix">
                                                                            <h2 class="field-label">Dịch giả:&nbsp;</h2>
                                                                            <div class="field-items">
                                                                                <div class="field-item even">${book.translator}</div>

                                                                            </div>
                                                                        </section>
                                                                    </c:if>
                                                                    <section class="field field-type-number-integer field-label-inline clearfix">
                                                                        <h2 class="field-label">Số trang:&nbsp;</h2>
                                                                        <div class="field-items"><div class="field-item even">${book.pageNumber}</div></div>
                                                                    </section>
                                                                    <section class="field field-type-text field-label-inline clearfix">
                                                                        <h2 class="field-label">Kích thước:&nbsp;</h2>
                                                                        <div class="field-items"><div class="field-item even">${book.pageSize}</div></div>
                                                                    </section>
                                                                    <section class="field field-type-number-decimal field-label-inline clearfix">
                                                                        <h2 class="field-label">Giá bìa:&nbsp;</h2>
                                                                        <div class="field-items"><div class="field-item even">${book.price} VND</div></div>
                                                                    </section>
                                                                    <div class="field field-type-markup field-label-hidden">
                                                                        <div class="field-items">
                                                                            <div class="field-item even">
                                                                                <div class="buttom-buy">
                                                                                    <button onclick="productService.addProduct('${book.id}','${book.author}','${book.title}',${book.price},'${book.imageUrl}')">Thêm vào giỏ hàng</button>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </fieldset>
                                                            <fieldset class="field-group-fieldset">
                                                                <h3>Giới thiệu sách:</h3>
                                                                <p>${book.description}</p>
                                                            </fieldset>
                                                        </div>
                                                    </article>
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
