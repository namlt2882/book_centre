var bookDetailModel;
var _bookDetailId;
var _bookDetailAuthor;
var _bookDetailTranslator;
var _bookDetailSize;
var _bookDetailPageNumber;
var _bookDetailPrice;
var _bookDetailDescription;
var _bookDetailTitle;
var _bookDetailImageUrl;
var _bookDetailUrl;
var _bookDetailTitle_input;
var _bookDetailImageUrl_input;
var _bookDetailUrl_input;
var _bookDetailIsbn;
window.addEventListener('load', function () {
    bookDetailModel = document.getElementById("book_detail_model");
    bookDetailClose = document.getElementsByClassName("book_detail_close")[0];
    bookDetailClose.onclick = function () {
        bookDetailModel.style.display = "none";
    };
    _bookDetailId = document.getElementById("book_detail_id");
    _bookDetailAuthor = document.getElementById("book_detail_author");
    _bookDetailTranslator = document.getElementById("book_detail_translator");
    _bookDetailSize = document.getElementById("book_detail_size");
    _bookDetailPageNumber = document.getElementById("book_detail_page_number");
    _bookDetailPrice = document.getElementById("book_detail_price");
    _bookDetailDescription = document.getElementById("book_detail_description");
    _bookDetailIsbn = document.getElementById("book_detail_isbn");
    _bookDetailTitle = document.getElementById("book_detail_title");
    _bookDetailImageUrl = document.getElementById("book_detail_imageUrl");
    _bookDetailUrl = document.getElementById("book_detail_url");
    _bookDetailTitle_input = document.getElementById("book_detail_title_input");
    _bookDetailImageUrl_input = document.getElementById("book_detail_imageUrl_input");
    _bookDetailUrl_input = document.getElementById("book_detail_url_input");
    //error
}, false);

function showBookDetailModel(id) {
    var book = findBook(id);
    var author = getBookAttribute(book, "author");
    var translator = getBookAttribute(book, "translator");
    var size = getBookAttribute(book, "pageSize");
    var pageNumber = getBookAttribute(book, "pageNumber");
    var price = getBookAttribute(book, "price");
    var description = getBookAttribute(book, "description");
    var title = getBookAttribute(book, "title");
    var imageUrl = getBookAttribute(book, "imageUrl");
    var url = getBookAttribute(book, "url");
    var isbn = getBookAttribute(book, "isbn");
    _bookDetailId.value = id;
    _bookDetailAuthor.value = htmlEntitiesDecode(author);
    _bookDetailTranslator.value = htmlEntitiesDecode(translator);
    _bookDetailSize.value = size;
    _bookDetailPageNumber.value = pageNumber;
    _bookDetailPrice.value = price;
    _bookDetailIsbn.value = isbn;
    _bookDetailDescription.innerHTML = htmlEntitiesDecode(description);
    _bookDetailTitle.textContent = htmlEntitiesDecode(title);
    _bookDetailImageUrl.src = imageUrl;
    _bookDetailUrl.href = url;
    _bookDetailTitle_input.value = htmlEntitiesDecode(title);
    _bookDetailImageUrl_input.value = imageUrl;
    _bookDetailUrl_input.value = url;
    bookDetailModel.style.display = "block";
}

function htmlEntitiesDecode(s) {
    var e = document.createElement("div");
    e.innerHTML = s;
    var rs = e.innerHTML;
    return rs;

}

function isNullOrEmpty(s) {
    return (s != null && s !== "") ? false : true;
}