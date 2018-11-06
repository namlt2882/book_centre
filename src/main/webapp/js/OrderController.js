var orderController;

window.addEventListener('load', function () {
    orderController = new OrderController();
}, false);

function OrderController() {
    this.cancelOrder = function (id) {
        if (confirm("Hành động này sẽ hủy đơn hàng. Xác nhận tiếp tục?")) {
            AJAZZ.postRequest("/rest/order/" + id.trim() + "/cancel", null,
                    function (xhr) {
                        if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
                            alert("Thành công! Đơn hàng mã số " + id.trim() + " đã bị hủy!");
                            window.location.reload();
                        } else if (xhr.readyState === XMLHttpRequest.DONE && xhr.status !== 200) {
                            alert("Tác vụ thất bại! Vui lòng thử lại sau.");
                        }
                    }, null);
        }
        return false;
    }
}

