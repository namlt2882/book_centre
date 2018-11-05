function CategoryLayout() {
    this.initLayout = function (tagId, categories, publisher, isSelected) {
        var holder = document.getElementById(tagId);
        if (holder === null) {
            return;
        }
        var option = document.createElement("option");
        option.textContent = "--";
        holder.innerHTML = "";
        holder.appendChild(option);
        for (var i = 0; i < categories.length; i++) {
            var category = categories[i];
            if (publisher === category.publisher) {
                option = document.createElement("option");
                option.textContent = category.name;
                option.value = category.url;
                if (isSelected !== null && isSelected(category)) {
                    option.selected = true;
                }
                holder.appendChild(option);
            }
        }
    };
}

function CategoryCache() {
    var xmlTree;
    this.initData = function (tagId) {
        var holder = document.getElementById(tagId);
        if (holder == null) {
            return;
        }
        var xmlData = holder.text;
        var parser = new DOMParser();
        this.xmlTree = parser.parseFromString(xmlData, "text/xml");
    };

    this.getAll = function () {
        var rs = [];
        var categories = this.xmlTree.getElementsByTagName("category");
        for (var i = 0; i < categories.length; i++) {
            var category = categories[i];
            rs.push(this.transformCategoryToObject(category));
        }
        return rs;
    };

    this.findCategoryByPublisher = function (publisher) {
        var categories = this.xmlTree.getElementsByTagName("category");
        for (var i = 0; i < categories.length; i++) {
            var category = categories[i];
            if (publisher === category.getAttribute("publisher")) {
                return this.transformCategoryToObject(category);
            }
        }
        return null;
    };

    this.findCategoryByUrl = function (url) {
        var categories = this.xmlTree.getElementsByTagName("category");
        for (var i = 0; i < categories.length; i++) {
            var category = categories[i];
            if (url === this.getCategoryAttribute(category, "url")) {
                return this.transformCategoryToObject(category);
            }
        }
        return null;
    };

    this.find = function (id) {
        var categories = this.xmlTree.getElementsByTagName("category");
        for (var i = 0; i < categories.length; i++) {
            var category = categories[i];
            if (id === category.getAttribute("id")) {
                return this.transformCategoryToObject(category);
            }
        }
        return null;
    };

    this.getCategoryAttribute = function (category, attributeName) {
        var attr = category.getElementsByTagName(attributeName)[0];
        if (attr === null) {
            return null;
        }
        var childNode = attr.childNodes[0];
        if (childNode === null) {
            return "";
        }
        return childNode.nodeValue;
    };

    this.transformCategoryToObject = function (category) {
        var rs = new Category();
        var utility = new Utility();
        rs.id = category.getAttribute("id");
        rs.publisher = category.getAttribute("publisher");
        rs.name = utility.htmlEntitiesDecode(this.getCategoryAttribute(category, "name"));
        rs.url = this.getCategoryAttribute(category, "url");
        return rs;
    };
}

function Category() {
    var id;
    var publisher;
    var name;
    var url;

    this.toXml = function () {
        var rs = "<category id=\"" + (this.id === null ? "" : this.id) +
                "\" publisher=\"" + (this.publisher == null ? "" : this.publisher) + "\">";
        rs += "<name>" + (this.name === null ? "" : this.name) + "</name>";
        rs += "<url>" + (this.url === null ? "" : this.url) + "</url>";
        rs += "</category>"
        return rs;
    }
}


