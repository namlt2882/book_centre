package namlt.xml.asm.prj.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Category", propOrder = {
    "id", "publisher", "name", "url"
})
@XmlRootElement(name = "category")
public class Category {

    @XmlAttribute(required = false, name = "id")
    private String id;
    @XmlAttribute(required = false, name = "publisher")
    private String publisher;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "url")
    private String url;

    public Category() {
    }

    public Category(String name, String url, String publisher) {
        this.name = name;
        this.url = url;
        this.publisher = publisher;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Override
    public String toString() {
        return "[name='" + name + "', url='" + url + "']";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
