window.addEventListener('load', function () {
    var urlParams = new URLSearchParams(window.location.search);
    var msg = urlParams.get('msg');
    if (msg != null) {
        alert(msg);
    }
}, false);