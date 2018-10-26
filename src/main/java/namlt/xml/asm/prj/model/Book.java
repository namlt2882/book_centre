/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package namlt.xml.asm.prj.model;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import namlt.xml.asm.prj.crawler.ValueIdentifiable;
import namlt.xml.asm.prj.crawler.ValueIdentifier;
import namlt.xml.asm.prj.crawler.ValueIdentifierImpl;
import namlt.xml.asm.prj.crawler.model.BookPropertiesEnum;

/**
 *
 * @author ADMIN
 */
@XmlRootElement
public class Book implements Serializable, ValueIdentifiable {

    private String id;
    private String title;
    private String pageSize;
    private Integer pageNumber;
    private String description;
    private String author;
    private String translator;
    private Double price;
    private Integer status;
    private String isbn;
    private List<Tag> tagList;
    private List<OrderDetail> orderDetailList;
    private String url;

    public Book() {
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
                + "[page number=" + pageNumber + "][status=" + status + "][url='" + url + "'][description='" + description + "']";
    }

}
