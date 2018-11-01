var productList;
var sumOfAllTag;
window.addEventListener("load", function () {
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
    for (var i = 0; i < productList.length; i++) {
        book = productList[i];
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
            removeProduct(this.book_id);
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
    sumOfAllTag.textContent = calculateSum();
}

function calculateSum(){
    var book;
    var sum = 0;
    for (var i = 0; i < productList.length; i++) {
        book = productList[i];
        if (book !== null) {
            sum += book.price * book.quantity;
        }
    }
    return sum;
}
function changeProductQuantity(id, quantity, sumTag) {
    initData();
    var product = getProduct(id);
    if (product !== null) {
        if (quantity <= 0) {
            removeProduct(id);
        } else {
            product.quantity = quantity;
            saveProductList();
            //update price
            sumTag.textContent = product.price * product.quantity;
            countSumOfAll();
        }
    }
}

function removeProduct(id) {
    initData();
    var book;
    for (var i = 0; i < productList.length; i++) {
        book = productList[i];
        if (book != null && book.id === id) {
            productList.splice(i, 1);
            saveProductList();
            initLayout();
        }
    }
    countSumOfAll();
}
function initData() {
    productList = localStorage.getItem("productList");
    if (productList === null) {
        productList = new Array();
        saveProductList();
    } else {
        productList = JSON.parse(productList);
    }
}

function addProduct(id, author, title, price, imageUrl) {
    initData();
    var product = getProduct(id);
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
        productList.push(product);
    }
    saveProductList();
    alert("Đã thêm giỏ hàng!");
}

function getProduct(id) {
    var book;
    for (var i = 0; i < productList.length; i++) {
        book = productList[i];
        if (book.id === id) {
            return book;
        }
    }
    return null;
}
function saveProductList() {
    localStorage.setItem("productList", JSON.stringify(productList));
}
