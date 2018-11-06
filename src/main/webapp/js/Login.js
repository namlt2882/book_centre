function login() {
    var usernameInput = document.getElementById("login_username");
    var passwordInput = document.getElementById("login_password");
    var redirectPage = document.getElementById("login_redirectPage");
    var username = usernameInput.value;
    var password = passwordInput.value;
    var data = "username=" + username + "&password=" + password;
    if (redirectPage !== null) {
        data += "&redirect_page=" + redirectPage.value;
    }
    AJAZZ.postRequest("/rest/auth/Login", null, function (xhr) {
        if (xhr.readyState == XMLHttpRequest.DONE && xhr.status === 200) {
            //successful action will result a redirect url
            window.location.href = xhr.responseText;
        } else if (xhr.readyState == XMLHttpRequest.DONE && xhr.status === 401) {
            alert("Username hoặc mật khẩu sai!");
        }
    }, data);
    return false;
}

function logout() {
    AJAZZ.postRequest("/rest/auth/Logout", null, function (xhr) {
        if (xhr.readyState == XMLHttpRequest.DONE) {
            window.location.href = "/";
            document.cookie = 'JSESSIONID=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
        }
    }, null);
}