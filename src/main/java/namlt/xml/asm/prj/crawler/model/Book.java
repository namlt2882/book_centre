/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package namlt.xml.asm.prj.crawler.model;

import namlt.xml.asm.prj.crawler.ValueIdentifierImpl;
import java.util.List;
import namlt.xml.asm.prj.crawler.ValueIdentifiable;
import namlt.xml.asm.prj.crawler.ValueIdentifier;

/**
 *
 * @author ADMIN
 */
public class Book implements ValueIdentifiable {

    private String name;
    private List<String> author;
    private String price;
    private String publishDay;
    private String pageSize;
    private String numberOfPage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAuthor() {
        return author;
    }

    public void setAuthor(List<String> author) {
        this.author = author;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPublishDay() {
        return publishDay;
    }

    public void setPublishDay(String publishDay) {
        this.publishDay = publishDay;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getNumberOfPage() {
        return numberOfPage;
    }

    public void setNumberOfPage(String numberOfPage) {
        this.numberOfPage = numberOfPage;
    }

    @Override
    public ValueIdentifier getIdentifier() {
        return new ValueIdentifierImpl(BookPropertiesEnum.class);
    }

}
