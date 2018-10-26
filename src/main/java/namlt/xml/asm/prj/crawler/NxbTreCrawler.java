/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package namlt.xml.asm.prj.crawler;

import namlt.xml.asm.prj.parser.BaseParser;
import namlt.xml.asm.prj.parser.BoundReachedException;
import namlt.xml.asm.prj.parser.ParserHelper;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import namlt.xml.asm.prj.parser.NestedTagResolver;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;
import namlt.xml.asm.prj.model.Book;
import static namlt.xml.asm.prj.utils.CommonUtils.parseInt;
import static namlt.xml.asm.prj.utils.CommonUtils.parseDouble;
import static namlt.xml.asm.prj.utils.InternetUtils.crawl;
import static namlt.xml.asm.prj.utils.InternetUtils.crawl;

/**
 *
 * @author ADMIN
 */
public class NxbTreCrawler extends BaseParser {

    private XMLInputFactory inputFactory = XMLInputFactory.newFactory();
    private ParserHelper parserHelper = new ParserHelper();

    public NxbTreCrawler() {
        inputFactory.setProperty(
                XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
        inputFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
    }

    public static void main(String[] args) {
        NxbTreCrawler crawler = new NxbTreCrawler();
//        Book book = crawler.crawlBookPage("https://www.nxbtre.com.vn/sach/44576.html");
//        System.out.println(book);
        List<Book> books = crawler.crawlNextNewBooks(50);
        System.out.println("Book number:" + books.size());
        books.forEach(System.out::println);

    }

    public Book crawlBookPage(String url) {
        Book book = null;
        ValueIdentifier identifier = new Book().getIdentifier();
        try {
            String htmlSource = crawl(url);
            if (htmlSource != null) {
                StringBuilder sb = new StringBuilder();
                NestedTagResolver.formatNestedTag(htmlSource, "html").forEach(sb::append);
                htmlSource = sb.toString();

            }
//            parserHelper.writeToFile(htmlSource);
            XMLStreamReader reader = inputFactory.createXMLStreamReader(new StringReader(htmlSource));
            int event;
            while (reader.hasNext()) {
                event = reader.next();
                if (event == START_ELEMENT) {
                    if (isTag("div", reader) && equalClass(reader, null, "aut-detail")) {
                        book = new Book(url);
                        parserHelper.skipToCharacter(reader);
                        StringBuilder sb = new StringBuilder();
                        parserHelper.readTextInside(reader, sb);
                        String title = sb.toString();
                        book.setTitle(title.trim());
                    } else if (isTag("ul", reader) && equalClass(reader, null, "itemDetail-cat")) {
                        Map<String, String> valueMap = new HashMap<>();
                        int tagCount = 1;

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
                        book.setAuthor(values.get("AUTHOR"));
                        book.setTranslator(values.get("TRANSLATOR"));
                        book.setPageSize(values.get("SIZE"));
                        String pageNumber = values.get("PAGE_NUMBER");
                        //process page number here
                        book.setPageNumber(parseInt(pageNumber).orElse(null));
                        book.setIsbn(values.get("ISBN"));
                        String price = values.get("PRICE");
                        //process price here
                        book.setPrice(parseDouble(price).orElse(null));
                        book.setDescription(values.get("DESCRIPTION"));
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return book;
    }

    public List<String> crawlNewBookUrls(String url) {
        List<String> urls = new ArrayList<>();
        try {
            String htmlSource = crawl(url);
            if (htmlSource != null) {
                StringBuilder sb = new StringBuilder();
                NestedTagResolver.formatNestedTag(htmlSource, "html").forEach(sb::append);
                htmlSource = sb.toString();

            }
            parserHelper.writeToFile(htmlSource);
            XMLStreamReader reader = inputFactory.createXMLStreamReader(new StringReader(htmlSource));
            int event;
            String link;
            while (reader.hasNext()) {
                event = reader.next();
                if (event == START_ELEMENT) {
                    if (isTag("div", reader) && equalClasses(reader, null, "bBox", "book-item")) {
                        parserHelper.skipTag(reader, "a");
                        link = reader.getAttributeValue("", "href");
                        if (link != null && !"".equals((link = link.trim()))) {
                            urls.add(link);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return urls;
    }

    public List<Book> crawlNextNewBooks(int time) {
        List<Book> rs = new ArrayList<>();
        String tmp = "https://www.nxbtre.com.vn/tu-sach/trang-";
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < time; i++) {
            String url = tmp + (i + 1) + '/';
            crawlNewBookUrls(url).forEach(s -> urls.add("https://www.nxbtre.com.vn" + s.replace("xem-them", "sach")));
        }
        urls.parallelStream().map(s -> crawlBookPage(s))
                .forEach(b -> {
                    synchronized (rs) {
                        rs.add(b);
                    }
                });
        return rs;
    }

}
