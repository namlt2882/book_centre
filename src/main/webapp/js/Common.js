window.addEventListener('load', function () {
    AJAZZ = new AjaxRequest();
    var urlParams = new URLSearchParams(window.location.search);
    var msg = urlParams.get('msg');
    if (msg != null) {
        alert(msg);
    }
}, false);

var AJAZZ;
function Utility() {
    this.htmlEntitiesDecode = function (s) {
        var e = document.createElement("div");
        e.textContent = s;
        var rs = e.textContent;
        return rs;
    }
}

function AjaxRequest() {
    this.getXMLHttpRequest = function () {
        var xhr;
        if (window.XMLHttpRequest) {
            xhr = new XMLHttpRequest();
        } else {
            xhr = new ActiveXObject('Microsoft.XMLHTTP');
        }
        if (!xhr) {
            alert("Trình duyệt của bạn không hỗ trợ Ajax");
        }
        return xhr;
    };

    this.postRequest = function (url, modifyXhr, callback, data) {
        this.sendRequest("POST", url, modifyXhr, callback, data);
    };

    this.getRequest = function (url, modifyXhr, callback, data) {
        this.sendRequest("GET", url, modifyXhr, callback, null);
    };

    this.sendRequest = function (method, url, modifyXhr, callback, data) {
        var xhr = this.getXMLHttpRequest();
        if (xhr === null) {
            return;
        }
        xhr.open(method, url, true);
        if (modifyXhr !== null) {
            modifyXhr(xhr);
        }
        if (callback !== null) {
            xhr.onreadystatechange = function () {
                callback(this);
            };
        }
        xhr.send(data);
    };
}