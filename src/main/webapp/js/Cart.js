var productService;
var cartView;
window.addEventListener("load", function () {
    productService = new ProductService();
    productService.initData();
    cartView = new CartView();
    cartView.initLayout();
}, false);

function CartView() {
    this.sumOfAllTag;
    this.initLayout = function () {
        const view = this;
        var cartContainer = document.getElementById("cart_container");
        if (cartContainer === null) {
            return;
        }
        cartContainer.innerHTML = "";
        var tr, td, book, sum, input, button;
        for (var i = 0; i < productService.productList.orderDetails.length; i++) {
            book = productService.productList.orderDetails[i];
            //create tr and append to context
            tr = document.createElement("tr");
            cartContainer.appendChild(tr);
            //id
            td = document.createElement("td");
            td.textContent = book.bookId;
            tr.appendChild(td);
            //title
            td = document.createElement("td");
            td.textContent = book.title;
            tr.appendChild(td);
            //author
            td = document.createElement("td");
            td.textContent = book.author;
            tr.appendChild(td);
            //price
            td = document.createElement("td");
            td.textContent = book.price;
            tr.appendChild(td);
            //sum
            sum = document.createElement("td");
            sum.textContent = book.price * book.quantity;
            //quantity
            td = document.createElement("td");
            input = document.createElement("input");
            input.type = "number";
            input.min = 1;
            input.value = book.quantity;
            input.book_id = book.bookId;
            input.sum = sum;
            input.onchange = function () {
                productService.changeProductQuantity(this.book_id, this.value, this.sum);
                view.countSumOfAll();
            };
            td.appendChild(input);
            tr.appendChild(td);
            tr.appendChild(sum);
            //actions
            //remove this product
            td = document.createElement("td");
            button = document.createElement("button");
            button.textContent = "Xóa";
            button.book_id = book.bookId;
            button.onclick = function () {
                productService.removeProduct(this.book_id);
                view.initLayout();
            };
            td.appendChild(button);
            tr.appendChild(td);
        }
        //sum of all
        tr = document.createElement("tr");
        cartContainer.appendChild(tr);

        td = document.createElement("td");
        td.setAttribute("colspan", "5");
        td.setAttribute("style", "text-align:right;");
        td.textContent = "Tổng cộng";
        tr.appendChild(td);
        td = document.createElement("td");
        this.sumOfAllTag = td;
        tr.appendChild(td);
        this.countSumOfAll();
    };

    this.checkout = function () {
        productService.initData();
        var order = productService.productList;
        if (order.orderDetails.length === 0) {
            alert("Quý khách không có sản phẩm trong giỏ hàng!");
            return;
        }
        if (confirm("Chắc chứ?")) {
            AJAZZ.postRequest("/rest/order", function (xhr) {
                xhr.setRequestHeader("Content-Type", "application/xml");
            }, function (xhr) {
                if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
                    alert("Tạo đơn hàng thành công! Trạng thái đơn hàng: MỚI");
                    localStorage.removeItem("productList");
                    window.location.reload();
                } else if (xhr.readyState === XMLHttpRequest.DONE && xhr.status !== 200) {
                    alert("Có lỗi xảy ra. Xin thử lại sau!");
                }
            }, localStorage.getItem("productList"));
        }
    };

    this.countSumOfAll = function () {
        this.sumOfAllTag.textContent = productService.calculateSum();
    };

}

function ProductService() {
    this.productList;

    this.changeProductQuantity = function (id, quantity, sumTag) {
        this.initData();
        var orderDetail = this.getProduct(id);
        if (orderDetail !== null) {
            if (quantity <= 0) {
                this.removeProduct(id);
            } else {
                orderDetail.quantity = quantity;
                this.saveProductList();
                //update price
                sumTag.textContent = orderDetail.price * orderDetail.quantity;
            }
        }
    };

    this.addProduct = function (id, author, title, price, imageUrl) {
        this.initData();
        var orderDetail = this.getProduct(id);
        if (orderDetail !== null) {
            orderDetail.quantity++;
        } else {
            this.productList.addOrderDetail(id, author, title, price, imageUrl, 1);
        }
        this.saveProductList();
        alert("Đã thêm giỏ hàng!");
    };

    this.initData = function () {
        var order = localStorage.getItem("productList");
        if (order === null || order === "undefined") {
            this.productList = new Order();
            this.saveProductList();
        } else {
            order = new OrderUnmarshaller().parseOrder(order);
            this.productList = order;
        }
    };

    this.calculateSum = function calculateSum() {
        var orderDetails = this.productList.orderDetails;
        var orderDetail;
        var sum = 0;
        for (var i = 0; i < orderDetails.length; i++) {
            orderDetail = orderDetails[i];
            if (orderDetail !== null) {
                sum += orderDetail.price * orderDetail.quantity;
            }
        }
        return sum;
    };

    this.removeProduct = function removeProduct(id) {
        id = id.trim();
        this.initData();
        var orderDetails = this.productList.orderDetails;
        var orderDetail;
        for (var i = 0; i < orderDetails.length; i++) {
            orderDetail = orderDetails[i];
            if (orderDetail !== null && orderDetail.bookId === id) {
                orderDetails.splice(i, 1);
                this.saveProductList();
            }
        }
    };

    this.getProduct = function getProduct(id) {
        id = id.trim();
        var orderDetail;
        var orderDetails = this.productList.orderDetails;
        for (var i = 0; i < orderDetails.length; i++) {
            orderDetail = orderDetails[i];
            if (orderDetail.bookId === id) {
                return orderDetail;
            }
        }
        return null;
    };

    this.saveProductList = function saveProductList() {
        localStorage.setItem("productList", "<?xml version=\"1.0\"?>" + this.productList.toXml());
    };
}

function Order() {
    this.id = null;
    this.customerId = null;
    this.status = null;
    this.orderDetails = [];

    this.addOrderDetail = function (id, author, title, price, imageUrl, quantity) {
        if (this.orderDetails === null) {
            this.orderDetails = [];
        }
        var orderDetail = new OrderDetail();
        orderDetail.orderId = this.id;
        orderDetail.bookId = id;
        orderDetail.author = author;
        orderDetail.title = title;
        orderDetail.price = price;
        orderDetail.imageUrl = imageUrl;
        orderDetail.quantity = quantity;
        this.orderDetails.push(orderDetail);
        return orderDetail;
    };

    this.toXml = function () {
        var rs = "<order id=\"" + (this.id === null ? "" : this.id) + "\">";
        rs += "<customerId>" + (this.customerId === null ? "" : this.customerId) + "</customerId>";
        rs += "<status>" + (this.status === null ? "" : this.status) + "</status>";
        rs += "<orderDetails>";
        for (var i = 0; i < this.orderDetails.length; i++) {
            var orderDetail = this.orderDetails[i];
            rs += orderDetail.toXml();
        }
        rs += "</orderDetails>";
        rs += "</order>";
        return rs;
    };

}

function OrderUnmarshaller() {
    this.getAttribute = function (node, attributeName) {
        var attr = node.getElementsByTagName(attributeName)[0];
        if (attr === null) {
            return null;
        }
        var childNode = null;
        if (attr.childNodes !== null && attr.childNodes.length > 0) {
            childNode = attr.childNodes[0];
        }
        if (childNode === null || typeof childNode === "undefined") {
            return "";
        }
        return childNode.nodeValue;
    };

    this.parseOrder = function (xmlData) {
        var rs;
        var xmlTree = new DOMParser().parseFromString(xmlData, "text/xml");
        var order = xmlTree.getElementsByTagName("order");
        if (order === null) {
            return null;
        }
        order = order[0];
        rs = new Order();
        rs.id = order.getAttribute("id").trim();
        rs.customerId = this.getAttribute(order, "customerId").trim();
        rs.status = this.getAttribute(order, "status").trim();
        var orderDetails = order.getElementsByTagName("orderDetails");
        if (orderDetails === null) {
            rs.orderDetails = [];
        } else {
            rs.orderDetails = this.parseOrderDetails(orderDetails[0]);
        }
        return rs;
    };

    this.parseOrderDetails = function (xmlFragments) {
        var fragments = xmlFragments.getElementsByTagName("orderDetail");
        if (fragments === null) {
            return [];
        }
        var rs = [];
        for (var i = 0; i < fragments.length; i++) {
            var fragment = fragments[i];
            var orderDetail = new OrderDetail();
            orderDetail.orderId = this.getAttribute(fragment, "orderId");
            orderDetail.bookId = this.getAttribute(fragment, "bookId");
            orderDetail.author = this.getAttribute(fragment, "author");
            orderDetail.title = this.getAttribute(fragment, "title");
            orderDetail.imageUrl = this.getAttribute(fragment, "imageUrl");
            orderDetail.quantity = this.getAttribute(fragment, "quantity");
            orderDetail.price = this.getAttribute(fragment, "itemPrice");
            rs.push(orderDetail);
        }
        return rs;
    };
}

function OrderDetail() {
    this.orderId = null;
    this.bookId = null;
    this.author = null;
    this.title = null;
    this.imageUrl = null;
    this.quantity = null;
    this.price = null;

    this.toXml = function () {
        var rs = "<orderDetail>";
        rs += "<orderId>" + (this.orderId === null ? "" : this.orderId.trim()) + "</orderId>";
        rs += "<bookId>" + (this.bookId === null ? "" : this.bookId.trim()) + "</bookId>";
        rs += "<author>" + (this.author === null ? "" : this.author.trim()) + "</author>";
        rs += "<title>" + (this.title === null ? "" : this.title.trim()) + "</title>";
        rs += "<imageUrl>" + (this.imageUrl === null ? "" : this.imageUrl.trim()) + "</imageUrl>";
        rs += "<quantity>" + (this.quantity === null ? "" : this.quantity) + "</quantity>";
        rs += "<itemPrice>" + (this.price === null ? "" : this.price) + "</itemPrice>";
        rs += "</orderDetail>";
        return rs;
    };
}
