<%@page contentType="text/html" pageEncoding="UTF-8"%>
<script type="text/javascript" src="/js/BookDetail.js"></script>
<link type="text/css" href="/css/BookDetail.css" rel="stylesheet">
<div id="book_detail_model">
    <div class="book_edit_form">
        <h1 class="book_detail_close book_detail_trigger">
            X
        </h1>
        <div class="book_detail_img_container">
            <a id="book_detail_url" href="http://nhanam.com.vn/sach/16686/mot-buoi-sang-kho-quen-va-nhung-ban-tinh-ca-d" title="Link gốc" target="_blank">
                <img id="book_detail_imageUrl" class="book_detail_img" src="http://static.nhanam.com.vn/thumb/0x320/crop/Books/Images/2018/10/6/O8HUBQAL.jpg"/>
            </a>
        </div>
        <div class="book_detail_info">
            <h2 id="book_detail_title">Giấc mơ hóa rồng</h2>
            <form action="">
                <table class="book_detail_attributes">
                    <tr>
                        <td>Mã sản phẩm:</td>
                        <td><input id="book_detail_id" type="text" name="id" disabled="true"></td>
                    </tr>
                    <tr>
                        <td>Tác giả:</td>
                        <td><input id="book_detail_author" type="text" name="author"></td>
                    </tr>
                    <tr>
                        <td>Dịch giả:</td>
                        <td><input id="book_detail_translator" type="text" name="translator"></td>
                    </tr>
                    <tr>
                        <td>Khổ sách:</td>
                        <td><input id="book_detail_size" type="text" name="size"></td>
                    </tr>
                    <tr>
                        <td>Số trang:</td>
                        <td><input id="book_detail_page_number" type="number" name="page_number"></td>
                    </tr>
                    <tr>
                        <td>Gía:</td>
                        <td><input id="book_detail_price" type="number" name="price" step="0.1"></td>
                    </tr>
                    <tr>
                        <td>Mô tả:</td>
                        <td><textarea id="book_detail_description" rows="8" cols="60" name="description"></textarea></td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>