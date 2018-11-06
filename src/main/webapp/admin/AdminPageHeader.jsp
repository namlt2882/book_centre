<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page session="false" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<link type="text/css" rel="stylesheet" href="/css/Custom.css">
<script type="text/javascript" src="/js/Login.js"></script>
<div class="region region-leaderboard">
    <div id="block-block-3" class="block block-block ">
        <div class="block-inner clearfix">
            <div class="block-content content">
                <div id="logo-banner">
                    <div>
                        <div id="block-nodeblock-2" class="block block-nodeblock ">
                            <div class="block-inner clearfix">
                                <div class="block-content content">
                                    <div id="node-2" class="article article-type-blocks clearfix" role="article">
                                        <div class="content">
                                            <div class="field field-name-body field-type-text-with-summary field-label-hidden">
                                                <div class="field-items">
                                                    <div class="field-item even">
                                                        <div>
                                                            <img alt="" src="/img/sachvuive.gif" class="img-size-150">
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>  
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div id="banner">
                        <div id="block-nodeblock-3" class="block block-nodeblock ">
                            <div class="block-inner clearfix">
                                <div class="block-content content">
                                    <div id="node-3" class="article article-type-blocks clearfix" role="article">
                                        <div class="content">
                                            <div class="field field-name-body field-type-text-with-summary field-label-hidden">
                                                <div class="field-items">
                                                    <div class="field-item even">
                                                        <div>
                                                            <h1>Quản lí nhà sách</h1>
                                                            <%
                                                                HttpSession httpSession = request.getSession(false);
                                                                if (httpSession != null && httpSession.getAttribute("USER") != null) {
                                                            %>
                                                            <button onclick="logout();">Đăng xuất</button>
                                                            <%
                                                            } else {
                                                            %>
                                                            <script>window.location.href="/";</script>
                                                            <%
                                                                }
                                                            %>

                                                        </div>
                                                    </div>
                                                </div>
                                            </div>  
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<header class="clearfix" role="banner">
    <hgroup class="element-invisible">
        <h1 id="site-name" class="element-invisible"><a href="/" title="Trang nhà" class="active">Nhã Nam</a></h1>
    </hgroup>

    <div class="region region-header">
        <div id="block-block-4" class="block block-block ">
            <div class="block-inner clearfix">
                <div class="block-content content">
                    <div id="menu-bar-today">
                        <div>
                            <nav id="block-nice-menus-1" class="block block-nice-menus " role="navigation"><div class="block-inner clearfix">
                                    <div class="block-content content">
                                        <ul class="nice-menu nice-menu-down sf-js-enabled" id="nice-menu-1">
                                            <li class="odd ">
                                                <a href="/admin/Home.jsp">Kho sách</a>
                                            </li>
                                            <li class="menuparent even menu-item">
                                                <a href="/admin/Publisher.jsp">Dữ liệu nhà xuất bản</a>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </nav>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div id="block-block-10" class="block block-block ">
            <div class="block-inner clearfix">
                <div class="block-content content">
                </div>
            </div>
        </div>

    </div>
</header>
