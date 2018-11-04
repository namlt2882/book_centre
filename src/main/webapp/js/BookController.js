var bookController;

window.addEventListener('load', function () {
    bookController = new BookController();
}, false);

function BookController() {
    this.add = function (book, successCallBack, failCallBack) {
        AJAZZ.postRequest("/rest/product", function (xhr) {
            xhr.setRequestHeader("Content-Type", "application/xml");
        }, function (xhr) {
            if (xhr.readyState == XMLHttpRequest.DONE && xhr.status === 200) {
                successCallBack(xhr);
            } else if (xhr.readyState == XMLHttpRequest.DONE && xhr.status === 404) {
                failCallBack(xhr);
            }
        }, "<?xml version='1.0'?>" + book.toXml());
    }

    this.update = function (book, successCallBack, failCallBack) {
        AJAZZ.sendRequest("PUT", "/rest/product", function (xhr) {
            xhr.setRequestHeader("Content-Type", "application/xml");
        }, function (xhr) {
            if (xhr.readyState == XMLHttpRequest.DONE && xhr.status === 200) {
                successCallBack(xhr);
            } else if (xhr.readyState == XMLHttpRequest.DONE && xhr.status === 404) {
                failCallBack(xhr);
            }
        }, "<?xml version='1.0'?>" + book.toXml());
    }

}