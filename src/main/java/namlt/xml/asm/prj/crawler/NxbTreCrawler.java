/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package namlt.xml.asm.prj.crawler;

import namlt.xml.asm.prj.parser.BoundReachedException;
import namlt.xml.asm.prj.parser.ParserHelper;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import namlt.xml.asm.prj.crawler.model.Book;
import namlt.xml.asm.prj.parser.NestedTagResolver;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;
import static namlt.xml.asm.prj.utils.InternetUtils.crawl;

/**
 *
 * @author ADMIN
 */
public class NxbTreCrawler {

    private XMLInputFactory inputFactory = XMLInputFactory.newFactory();
    private ParserHelper parserHelper = new ParserHelper();

    public static void main(String[] args) {
        NxbTreCrawler crawler = new NxbTreCrawler();
        crawler.crawBookPage("https://www.nxbtre.com.vn/sach/44576.html");
    }

    public Book crawBookPage(String url) {
        Book book = null;
        ValueIdentifier identifier = new Book().getIdentifier();
        try {
            String htmlSource = crawl(url);
            if (htmlSource != null) {
                StringBuilder sb = new StringBuilder();
                NestedTagResolver.formatNestedTag(htmlSource, "html").forEach(sb::append);
                htmlSource = sb.toString();

            }
            parserHelper.writeToFile(htmlSource);
            inputFactory.setProperty(
                    XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
            XMLStreamReader reader = inputFactory.createXMLStreamReader(new StringReader(htmlSource));
            int event;
            while (reader.hasNext()) {
                event = reader.next();
                if (event == START_ELEMENT && "ul".equals(reader.getLocalName())
                        && "itemDetail-cat".equals(reader.getAttributeValue("", "class"))) {
                    Map<String, String> valueMap = new HashMap<>();
                    int tagCount = 1;
//                    String title = crawlerUtils.readTextInside(reader, 1);
//                    System.out.println(title);
                    StringBuilder sb;
                    boolean isFinished = false;
                    try {
                        while (tagCount > 0) {
                            parserHelper.setMaxTag(tagCount);
                            tagCount += parserHelper.skipTag(reader, "li");
                            parserHelper.setMaxTag(tagCount);
                            tagCount += parserHelper.skipToCharacter(reader);
                            tagCount += parserHelper.readTextInside(reader, (sb = new StringBuilder()));
                            String key = sb.toString();

                            parserHelper.setMaxTag(tagCount);
                            tagCount += parserHelper.skipToCharacter(reader);
                            tagCount += parserHelper.readTextInside(reader, (sb = new StringBuilder()));
                            try {
                                parserHelper.setMaxTag(tagCount);
                                tagCount += parserHelper.skipTag(reader, "li", sb);
                            } catch (BoundReachedException e) {
                                isFinished = true;
                            }
                            String value = sb.toString();
                            identifier.indentify(key != null ? key.trim() : null,
                                    value != null ? value.trim() : null);
                            if (isFinished) {
                                break;
                            }
                        }
                    } catch (Exception e) {
                    }
                    Map<String, String> values = identifier.values();
                    System.out.println(values.get("AUTHOR"));
                    System.out.println(values.get("TRANSLATOR"));
                    System.out.println(values.get("SIZE"));
                    System.out.println(values.get("PAGE_NUMBER"));
                    System.out.println(values.get("ISBN"));
                    System.out.println(values.get("PRICE"));
                    System.out.println(values.get("DESCRIPTION"));
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return book;
    }

}
