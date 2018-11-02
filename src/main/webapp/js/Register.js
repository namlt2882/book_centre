function register() {
    var phoneRegex = /^[0-9]{10,11}$/;
    var usernameInput = document.getElementById("register_username");
    var passwordInput = document.getElementById("register_password");
    var comfirmInput = document.getElementById("register_confirm");
    var addressInput = document.getElementById("register_address");
    var phoneInput = document.getElementById("register_phone");
    var nameInput = document.getElementById("register_name");
    var redirectLink = document.getElementById("register_redirectUrl");
    if (passwordInput.value !== comfirmInput.value) {
        alert("Nhập lại mật khẩu chưa đúng!");
    } else if (!phoneInput.value.match(phoneRegex)) {
        alert("Số điện thoại không hợp lệ!");
    } else {
        var username = usernameInput.value;
        var password = passwordInput.value;
        var address = addressInput.value;
        var phone = phoneInput.value;
        var name = nameInput.value;
        var data = "username=" + username + "&password=" + password +
                "&address=" + address + "&phone=" + phone + "&name=" + name;
        AJAZZ.postRequest("/rest/auth/Register", null, function (xhr) {
            if (xhr.readyState == XMLHttpRequest.DONE && xhr.status === 200) {
                var link = redirectLink.href;
                window.location.href = link;
            } else if (xhr.readyState == XMLHttpRequest.DONE && xhr.status === 404) {
                alert("Đăng kí thất bại, xin thử lại!");
            }
        }, data);
    }
    return false;
}