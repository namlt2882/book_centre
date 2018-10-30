var bookDetailModel;
window.addEventListener('load', function () {
    bookDetailModel = document.getElementById("book_detail_model");
    bookDetailClose = document.getElementsByClassName("book_detail_close")[0];
    bookDetailClose.onclick = function () {
        bookDetailModel.style.display = "none";
    }
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
    document.getElementById("book_detail_id").value = id;
    document.getElementById("book_detail_author").value = htmlEntitiesDecode(author);
    document.getElementById("book_detail_translator").value = htmlEntitiesDecode(translator);
    document.getElementById("book_detail_size").value = size;
    document.getElementById("book_detail_page_number").value = pageNumber;
    document.getElementById("book_detail_price").value = price;
    document.getElementById("book_detail_description").innerHTML = htmlEntitiesDecode(description);
    document.getElementById("book_detail_title").textContent = htmlEntitiesDecode(title);
    document.getElementById("book_detail_imageUrl").src = imageUrl;
    document.getElementById("book_detail_url").href = url;
    bookDetailModel.style.display = "block";
}

function htmlEntitiesDecode(s){
    var e = document.createElement("div");
    e.innerHTML = s;
    var rs = e.innerHTML;
    return rs;
    
}
