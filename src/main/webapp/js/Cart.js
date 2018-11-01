var sumOfAllTag;
var productService;
window.addEventListener("load", function () {
    productService = new ProductService();
    initData();
    initLayout();
}, false);

function initLayout() {
    var cartContainer = document.getElementById("cart_container");
    if (cartContainer === null) {
        return;
    }
    cartContainer.innerHTML = "";
    var tr, td, book, sum, input, button;
    for (var i = 0; i < productService.productList.length; i++) {
        book = productService.productList[i];
        //create tr and append to context
        tr = document.createElement("tr");
        cartContainer.appendChild(tr);
        //id
        td = document.createElement("td");
        td.textContent = book.id;
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
        input.value = book.quantity;
        input.book_id = book.id;
        input.sum = sum;
        input.onchange = function () {
            changeProductQuantity(this.book_id, this.value, this.sum);
        }
        td.appendChild(input);
        tr.appendChild(td);
        tr.appendChild(sum);
        //actions
        //remove this product
        td = document.createElement("td");
        button = document.createElement("button");
        button.textContent = "Xóa";
        button.book_id = book.id;
        button.onclick = function () {
            productService.removeProduct(this.book_id);
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
    sumOfAllTag = td;
    tr.appendChild(td);
    countSumOfAll();
}

function checkout() {
    if (confirm("Chắc chứ?")) {
        localStorage.removeItem("productList");
        window.location.reload();
    }
}

function countSumOfAll() {
    sumOfAllTag.textContent = productService.calculateSum();
}


function changeProductQuantity(id, quantity, sumTag) {
    initData();
    var product = productService.getProduct(id);
    if (product !== null) {
        if (quantity <= 0) {
            productService.removeProduct(id);
        } else {
            product.quantity = quantity;
            productService.saveProductList();
            //update price
            sumTag.textContent = product.price * product.quantity;
            countSumOfAll();
        }
    }
}

function initData() {
    var productList = localStorage.getItem("productList");
    if (productList === null || productList === "undefined") {
        productList = new Array();
        productService.saveProductList();
    } else {
        productList = JSON.parse(productList);
    }
    productService.productList = productList;
}

function addProduct(id, author, title, price, imageUrl) {
    initData();
    var product = productService.getProduct(id);
    if (product != null) {
        product.quantity++;
    } else {
        product = {
            id: id,
            author: author,
            title: title,
            price: price,
            imageUrl: imageUrl,
            quantity: 1
        }
        productService.productList.push(product);
    }
    productService.saveProductList();
    alert("Đã thêm giỏ hàng!");
}

function ProductService() {
    this.productList;
    this.calculateSum = function calculateSum() {
        var book;
        var sum = 0;
        for (var i = 0; i < this.productList.length; i++) {
            book = this.productList[i];
            if (book !== null) {
                sum += book.price * book.quantity;
            }
        }
        return sum;
    };

    this.removeProduct = function removeProduct(id) {
        initData();
        var book;
        for (var i = 0; i < this.productList.length; i++) {
            book = this.productList[i];
            if (book != null && book.id === id) {
                this.productList.splice(i, 1);
                this.saveProductList();
                initLayout();
            }
        }
    };

    this.getProduct = function getProduct(id) {
        var book;
        for (var i = 0; i < this.productList.length; i++) {
            book = this.productList[i];
            if (book.id === id) {
                return book;
            }
        }
        return null;
    };

    this.saveProductList = function saveProductList() {
        localStorage.setItem("productList", JSON.stringify(this.productList));
    };
}
