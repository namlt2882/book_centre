var bookController;

window.addEventListener('load', function () {
    bookController = new BookController();
}, false);

function BookController() {

    this.addListBook = function (books, successCallBack, failCallBack) {
        var data = "<?xml version='1.0'?><books>";
        for (var i = 0; i < books.length; i++) {
            data += books[i].toXml();
        }
        data += "</books>";
        AJAZZ.postRequest("/rest/product/list", function (xhr) {
            xhr.setRequestHeader("Content-Type", "application/xml");
        }, function (xhr) {
            if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
                successCallBack(xhr);
            } else if (xhr.readyState === XMLHttpRequest.DONE && xhr.status !== 200) {
                failCallBack(xhr);
            }
        }, data);
    }

    this.add = function (book, successCallBack, failCallBack) {
        AJAZZ.postRequest("/rest/product", function (xhr) {
            xhr.setRequestHeader("Content-Type", "application/xml");
        }, function (xhr) {
            if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
                successCallBack(xhr);
            } else if (xhr.readyState === XMLHttpRequest.DONE && xhr.status !== 200) {
                failCallBack(xhr);
            }
        }, "<?xml version='1.0'?>" + book.toXml());
    }

    this.update = function (book, successCallBack, failCallBack) {
        AJAZZ.sendRequest("PUT", "/rest/product", function (xhr) {
            xhr.setRequestHeader("Content-Type", "application/xml");
        }, function (xhr) {
            if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
                successCallBack(xhr);
            } else if (xhr.readyState === XMLHttpRequest.DONE && xhr.status !== 200) {
                failCallBack(xhr);
            }
        }, "<?xml version='1.0'?>" + book.toXml());
    }

    this.setActiveBook = function (id) {
        id = id.trim();
        if (confirm("Sản phẩm có mã '" + id + "' sẽ được kinh doanh trở lại. Xác nhận?")) {
            AJAZZ.postRequest("/rest/product/" + id + "/SetActive", null,
                    function (xhr) {
                        if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
                            alert("Thành công");
                            window.location.reload();
                        } else if (xhr.readyState === XMLHttpRequest.DONE && xhr.status !== 200) {
                            alert("Thao tác thất bại! Vui lòng thử lại sau!");
                        }
                    }, null);
        }
    };

    this.setDisableBook = function (id) {
        id = id.trim();
        if (confirm("Sản phẩm có mã '" + id + "' sẽ ngừng kinh doanh. Xác nhận?")) {
            AJAZZ.postRequest("/rest/product/" + id + "/SetDisable", null,
                    function (xhr) {
                        if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
                            alert("Thành công");
                            window.location.reload();
                        } else if (xhr.readyState === XMLHttpRequest.DONE && xhr.status !== 200) {
                            alert("Thao tác thất bại! Vui lòng thử lại sau!");
                        }
                    }, null);
        }
    };

    this.setOutOfStockBook = function (id) {
        id = id.trim();
        if (confirm("Sản phẩm có mã '" + id + "' tạm thời hết hàng. Xác nhận?")) {
            AJAZZ.postRequest("/rest/product/" + id + "/SetOutOfStock", null,
                    function (xhr) {
                        if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
                            alert("Thành công");
                            window.location.reload();
                        } else if (xhr.readyState === XMLHttpRequest.DONE && xhr.status !== 200) {
                            alert("Thao tác thất bại! Vui lòng thử lại sau!");
                        }
                    }, null);
        }
    };

}