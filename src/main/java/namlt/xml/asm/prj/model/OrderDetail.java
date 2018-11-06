package namlt.xml.asm.prj.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderDetail", propOrder = {
    "orderId", "bookId", "author",
    "title", "imageUrl", "quantity", "itemPrice"})
@XmlRootElement(name = "orderDetail")
public class OrderDetail implements Serializable {

    @XmlElement(name = "orderId", required = false)
    private Integer orderId;
    @XmlElement(name = "bookId")
    private String bookId;
    @XmlElement(name = "author")
    private String author;
    @XmlElement(name = "title")
    private String title;
    @XmlElement(name = "imageUrl")
    private String imageUrl;
    @XmlSchemaType(name = "positiveInteger")
    @XmlElement(name = "quantity")
    private int quantity;
    @XmlElement(name = "itemPrice")
    private double itemPrice;

    public OrderDetail() {
    }

    public String getAuthor() {
        return author;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

}
