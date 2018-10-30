var xmlTree;
window.onload = function () {
    initData();
}
function initData() {
    var holder = document.getElementById("xmlData");
    if (holder == null) {
        return;
    }
    var xmlData = holder.text;
    var parser = new DOMParser();
    xmlTree = parser.parseFromString(xmlData, "text/xml");
    console.log("Quantity of book=" + xmlTree.getElementsByTagName("book").length);
    var books = xmlTree.getElementsByTagName("book");
    for (var i = 0; i < books.length; i++) {
        var book = books[i];
        console.log("[id=" + book.getAttribute("id") + "]"
                + "[title=" + getBookAttribute(book, "title") + "]")
    }
}

function findBook(id) {
    var books = xmlTree.getElementsByTagName("book");
    for (var i = 0; i < books.length; i++) {
        var book = books[i];
        if (id === book.getAttribute("id")) {
            return book;
        }
    }
    return null;
}
function getBookAttribute(book, attributeName) {
    var attr = book.getElementsByTagName(attributeName)[0];
    if (attr == null) {
        return null;
    }
    return attr.childNodes[0].nodeValue;
}