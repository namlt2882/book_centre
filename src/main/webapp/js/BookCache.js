var bookCache;

window.addEventListener('load', function () {
    bookCache = new BookCache();
    bookCache.initData();
}, false);

function BookCache() {
    var xmlTree;
    this.initData = function () {
        var holder = document.getElementById("xmlData");
        if (holder == null) {
            return;
        }
        var xmlData = holder.text;
        var parser = new DOMParser();
        this.xmlTree = parser.parseFromString(xmlData, "text/xml");
        console.log("Quantity of book=" + this.xmlTree.getElementsByTagName("book").length);
        var books = this.xmlTree.getElementsByTagName("book");
        for (var i = 0; i < books.length; i++) {
            var book = books[i];
            console.log("[id=" + book.getAttribute("id") + "]"
                    + "[title=" + this.getBookAttribute(book, "title") + "]")
        }
    }

    this.findBook = function (id) {
        var books = this.xmlTree.getElementsByTagName("book");
        for (var i = 0; i < books.length; i++) {
            var book = books[i];
            if (id === book.getAttribute("id")) {
                return this.transformBookToObject(book);
            }
        }
        return null;
    }

    this.getBookAttribute = function (book, attributeName) {
        var attr = book.getElementsByTagName(attributeName)[0];
        if (attr == null) {
            return null;
        }
        var childNode = attr.childNodes[0];
        if (childNode == null) {
            return "";
        }
        return childNode.nodeValue;
    }

    this.transformBookToObject = function (book) {
        var rs = new Book();
        rs.id = book.getAttribute("id");
        rs.title = this.getBookAttribute(book, "title");
        rs.author = this.getBookAttribute(book, "author");
        rs.isbn = this.getBookAttribute(book, "isbn");
        rs.translator = this.getBookAttribute(book, "translator");
        rs.pageSize = this.getBookAttribute(book, "pageSize");
        rs.pageNumber = this.getBookAttribute(book, "pageNumber");
        rs.price = this.getBookAttribute(book, "price");
        rs.url = this.getBookAttribute(book, "url");
        rs.imageUrl = this.getBookAttribute(book, "imageUrl");
        rs.status = this.getBookAttribute(book, "status");
        rs.quantity = this.getBookAttribute(book, "quantity");
        rs.existedInDb = this.getBookAttribute(book, "existedInDb");
        rs.description = this.getBookAttribute(book, "description");
        return rs;
    }
}

function Book() {
    var id;
    var title;
    var author;
    var isbn;
    var translator;
    var pageSize;
    var pageNumber;
    var price;
    var url;
    var imageUrl;
    var status;
    var quantity;
    var existedInDb;
    var description;

    this.toXml = function () {
        var rs = "<book id=\"" + this.id + "\">";
        rs += "<title>" + this.title + "</title>";
        rs += "<author>" + this.author + "</author>";
        rs += "<isbn>" + this.isbn + "</isbn>";
        rs += "<translator>" + this.translator + "</translator>";
        rs += "<pageSize>" + this.pageSize + "</pageSize>";
        rs += "<pageNumber>" + this.pageNumber + "</pageNumber>";
        rs += "<price>" + this.price + "</price>";
        rs += "<url>" + this.url + "</url>";
        rs += "<imageUrl>" + this.imageUrl + "</imageUrl>";
        rs += "<status>" + this.status + "</status>";
        rs += "<quantity>" + this.quantity + "</quantity>";
        rs += "<existedInDb>" + this.existedInDb + "</existedInDb>";
        rs += "<description>" + this.description + "</description>";
        rs += "</book>"
        return rs;
    }
}