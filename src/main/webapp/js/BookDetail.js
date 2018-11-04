var bookDetailFrame;

window.addEventListener('load', function () {
    bookDetailFrame = new BookDetailFrame();
    bookDetailFrame.init();
}, false);

function BookDetailFrame() {
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

    this.addOrUpdate = function () {
        var id = this._bookDetailId.value;
        var book = bookCache.findBook(id);
        if (book != null) {
            var existedInDb = bookCache.getBookAttribute(book, "existedInDb");
            if (existedInDb == true) {

            } else {

            }
        } else {
            alert("Đã xảy ra lỗi! Vui lòng thử lại sau!");
        }
        return false;
    }

    this.init = function () {
        this.bookDetailModel = document.getElementById("book_detail_model");
        this.bookDetailClose = document.getElementsByClassName("book_detail_close")[0];
        this.bookDetailClose.model = this.bookDetailModel;
        this.bookDetailClose.onclick = function () {
            this.model.style.display = "none";
        };
        this._bookDetailId = document.getElementById("book_detail_id");
        this._bookDetailAuthor = document.getElementById("book_detail_author");
        this._bookDetailTranslator = document.getElementById("book_detail_translator");
        this._bookDetailSize = document.getElementById("book_detail_size");
        this._bookDetailPageNumber = document.getElementById("book_detail_page_number");
        this._bookDetailPrice = document.getElementById("book_detail_price");
        this._bookDetailDescription = document.getElementById("book_detail_description");
        this._bookDetailIsbn = document.getElementById("book_detail_isbn");
        this._bookDetailTitle = document.getElementById("book_detail_title");
        this._bookDetailImageUrl = document.getElementById("book_detail_imageUrl");
        this._bookDetailUrl = document.getElementById("book_detail_url");
        this._bookDetailTitle_input = document.getElementById("book_detail_title_input");
        this._bookDetailImageUrl_input = document.getElementById("book_detail_imageUrl_input");
        this._bookDetailUrl_input = document.getElementById("book_detail_url_input");
        //error
    }

    this.htmlEntitiesDecode = function (s) {
        var e = document.createElement("div");
        e.innerHTML = s;
        var rs = e.innerHTML;
        return rs;

    }

    this.isNullOrEmpty = function (s) {
        return (s != null && s !== "") ? false : true;
    }

    this.showBookDetailModel = function (id) {
        var bookObj = bookCache.findBook(id);
        console.log(bookObj.toXml());
        var author = bookObj.author;
        var translator = bookObj.translator;
        var size = bookObj.pageSize;
        var pageNumber = bookObj.pageNumber;
        var price = bookObj.price;
        var description = bookObj.description;
        var title = bookObj.title;
        var imageUrl = bookObj.imageUrl;
        var url = bookObj.url;
        var isbn = bookObj.isbn;
        this._bookDetailId.value = id;
        this._bookDetailAuthor.value = this.htmlEntitiesDecode(author);
        this._bookDetailTranslator.value = this.htmlEntitiesDecode(translator);
        this._bookDetailSize.value = size;
        this._bookDetailPageNumber.value = pageNumber;
        this._bookDetailPrice.value = price;
        this._bookDetailIsbn.value = isbn;
        this._bookDetailDescription.innerHTML = this.htmlEntitiesDecode(description);
        this._bookDetailTitle.textContent = this.htmlEntitiesDecode(title);
        this._bookDetailImageUrl.src = imageUrl;
        this._bookDetailUrl.href = url;
        this._bookDetailTitle_input.value = this.htmlEntitiesDecode(title);
        this._bookDetailImageUrl_input.value = imageUrl;
        this._bookDetailUrl_input.value = url;
        this.bookDetailModel.style.display = "block";
    }
}




