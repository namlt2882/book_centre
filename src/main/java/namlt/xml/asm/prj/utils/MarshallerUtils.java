package namlt.xml.asm.prj.utils;

import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import namlt.xml.asm.prj.model.Book;

public class MarshallerUtils {

    public static void main(String[] args) throws JAXBException {
        System.out.println(MarshallerUtils.marshall(new Book("abc")));
    }

    public static <T> String marshall(Object objs, Class... clazz) throws JAXBException {
        StringWriter sb = new StringWriter();
        Class[] objectClass;
        if (clazz.length <= 0) {
            objectClass = new Class[]{objs.getClass()};
        } else {
            objectClass = clazz;
        }
        JAXBContext jaxbc = JAXBContext.newInstance(objectClass);
        Marshaller marshaller = jaxbc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
        marshaller.marshal(objs, sb);
        return sb.toString();
    }

    public static <T> JAXBElement newJaxbElement(String localName, Class<T> clazz, Object value) {
        return new JAXBElement(new QName(localName), clazz, value);
    }
}
