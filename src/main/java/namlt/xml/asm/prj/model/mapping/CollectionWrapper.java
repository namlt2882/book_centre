package namlt.xml.asm.prj.model.mapping;

import java.util.List;
import javax.xml.bind.annotation.XmlAnyElement;

public class CollectionWrapper<T> {

    private List<T> items;

    public CollectionWrapper() {
    }

    public CollectionWrapper(List<T> items) {
        this.items = items;
    }

    @XmlAnyElement(lax = true)
    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

}
