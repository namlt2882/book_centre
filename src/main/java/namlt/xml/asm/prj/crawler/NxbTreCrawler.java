/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package namlt.xml.asm.prj.crawler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import namlt.xml.asm.prj.crawler.model.Book;
import namlt.xml.asm.prj.parser.NestedTagResolver;
import static namlt.xml.asm.prj.utils.InternetUtils.crawl;

/**
 *
 * @author ADMIN
 */
public class NxbTreCrawler {

    XMLInputFactory inputFactory = XMLInputFactory.newFactory();

    public static void main(String[] args) {
        NxbTreCrawler crawler = new NxbTreCrawler();
        crawler.crawBookPage(" https://www.nxbtre.com.vn/xem-them/44576.html");
    }
    int tagCount = 0;

    public Book crawBookPage(String url) {
        Book book = null;
        try {
            String htmlSource = crawl(url);
            if (htmlSource != null) {
                StringBuilder sb = new StringBuilder();
                NestedTagResolver.formatNestedTag(htmlSource, "html").forEach(sb::append);
                htmlSource = sb.toString();

            }
//            System.out.println(htmlSource);
            writeToFile(htmlSource);
            inputFactory.setProperty(
                    XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
            XMLStreamReader reader = inputFactory.createXMLStreamReader(new StringReader(htmlSource));
            int event;
            boolean inProduct = false;
            while (reader.hasNext()) {
                event = reader.next();
                if (event == XMLStreamConstants.START_ELEMENT && "div".equals(reader.getLocalName())
                        && "aut-detail".equals(reader.getAttributeValue("", "class"))) {
                    inProduct = true;
                    skipTag(reader, 2);
                    reader.next();
                    System.out.println(reader.getText().trim());
                    skipTag(reader, 4);
                    reader.next();
                    System.out.print(reader.getText().trim());
                    reader.next();
                    System.out.print(reader.getText().trim());
                    reader.next();
                    System.out.println(reader.getText().trim());
                    skipTag(reader, 3);
                    reader.next();
                    System.out.println(reader.getText().trim());
                    skipTag(reader, 2);
                    reader.next();
                    System.out.println(reader.getText().trim());
                    skipTag(reader, 2);
                    reader.next();
                    System.out.println(reader.getText().trim());
                    skipTag(reader, 2);
                    reader.next();
                    System.out.println(reader.getText().trim());
                    skipTag(reader, 2);
                    reader.next();
                    System.out.println(reader.getText().trim());
                    skipTag(reader, 3);
                    reader.next();
                    System.out.println(reader.getText().trim());
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return book;
    }

    public void skipTag(XMLStreamReader streamReader, int time) throws XMLStreamException {
        while (streamReader.hasNext()) {
            if (time <= 0) {
                break;
            }
            try {
                streamReader.nextTag();
            } catch (Exception e) {
                streamReader.next();
            }
            if (streamReader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                time--;
            }
        }
    }

    public static void writeToFile(String xmlSource) throws UnsupportedEncodingException, FileNotFoundException, IOException {
        ClassLoader classLoader = NxbTreCrawler.class.getClassLoader();
        String fileLocation = classLoader.getResource("crawler/schema/output.xml").getFile();
        File file = new File(URLDecoder.decode(fileLocation, "UTF-8"));
        FileOutputStream fw = new FileOutputStream(file);
        StringReader reader = new StringReader(xmlSource);
        int a;
        while ((a = reader.read()) != -1) {
            fw.write((char) a);
        }
        fw.flush();
        fw.close();
    }
}
