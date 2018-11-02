package namlt.xml.asm.prj.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import namlt.xml.asm.prj.crawler.ValueIdentifiable;
import namlt.xml.asm.prj.crawler.ValueIdentifier;
import namlt.xml.asm.prj.crawler.ValueIdentifierImpl;
import namlt.xml.asm.prj.crawler.model.BookPropertiesEnum;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Book", propOrder = {
    "id", "title", "author", "isbn",
    "translator", "pageSize", "pageNumber",
    "price", "url", "imageUrl", "status", "quantity", "existedInDb", "description"})
@XmlRootElement(name = "book")
public class Book implements Serializable, ValueIdentifiable {

    @XmlAttribute(name = "id")
    private String id;
    @XmlElement(name = "title")
    private String title;
    @XmlElement(name = "pageSize")
    private String pageSize;
    @XmlElement(name = "pageNumber")
    private Integer pageNumber;
    @XmlElement(name = "description")
    private String description;
    @XmlElement(name = "author")
    private String author;
    @XmlElement(name = "translator")
    private String translator;
    @XmlElement(name = "price")
    private Double price;
    @XmlElement(name = "status")
    private Integer status;
    @XmlElement(name = "isbn")
    private String isbn;
    @XmlElement(name = "url")
    private String url;
    @XmlElement(name = "imageUrl")
    private String imageUrl;
    @XmlElement(required = false, name = "quantity")
    private int quantity = 0;
    @XmlElement(required = false, name = "existedInDb")
    private boolean existedInDb = false;
    @XmlTransient
    private Date insertDate;
    @XmlTransient
    private List<Tag> tagList;
    @XmlTransient
    private List<OrderDetail> orderDetailList;

    public Book() {
    }

    public Book(String id, String title, String author, Double price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
    }

    public boolean isExistedInDb() {
        return existedInDb;
    }

    public void setExistedInDb(boolean existedInDb) {
        this.existedInDb = existedInDb;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Book(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTranslator() {
        return translator;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @XmlTransient
    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    @XmlTransient
    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    @Override
    public ValueIdentifier getIdentifier() {
        return new ValueIdentifierImpl(BookPropertiesEnum.class);
    }

    @Override
    public String toString() {
        return "[Book: '" + title + "'][id=" + id + "][isbn=" + isbn + "][author=" + author + "]"
                + "[translator=" + translator + "][page size=" + pageSize + "][price=" + price + "]"
                + "[page number=" + pageNumber + "][status=" + status + "][image url='" + imageUrl + "'][url='" + url + "']";
    }

}
