/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package namlt.xml.asm.prj.utils;

import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import namlt.xml.asm.prj.model.Book;

/**
 *
 * @author ADMIN
 */
public class MarshallerUtils {

    public static void main(String[] args) throws JAXBException {
        System.out.println(MarshallerUtils.marshall(new Book("abc")));
    }

    public static String marshall(Object objs) throws JAXBException {
        StringWriter sb = new StringWriter();
        JAXBContext jaxbc = JAXBContext.newInstance(objs.getClass());
        Marshaller marshaller = jaxbc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
        marshaller.marshal(objs, sb);
        return sb.toString();
    }
}
