package namlt.xml.asm.prj.crawler;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import namlt.xml.asm.prj.parser.BaseParser;
import namlt.xml.asm.prj.parser.NestedTagResolver;
import namlt.xml.asm.prj.parser.ParserHelper;
import static namlt.xml.asm.prj.utils.InternetUtils.crawl;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;
import namlt.xml.asm.prj.model.Book;
import namlt.xml.asm.prj.parser.BoundReachedException;
import static namlt.xml.asm.prj.utils.CommonUtils.parseInt;
import static namlt.xml.asm.prj.utils.CommonUtils.parseDouble;
import static namlt.xml.asm.prj.utils.InternetUtils.crawl;

public class NhaNamCrawler extends BaseParser {

    private XMLInputFactory inputFactory = XMLInputFactory.newFactory();

    public NhaNamCrawler() {
        inputFactory.setProperty(
                XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
        inputFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
    }

    public static void main(String[] args) {
        NhaNamCrawler crawler = new NhaNamCrawler();
//        crawler.crawlNewBookUrls("http://nhanam.com.vn/sach-moi-xuat-ban?page=1").forEach(System.out::println);
        System.out.println(crawler.crawlBookPage("http://nhanam.com.vn/sach/16684/gau-a-cau-on-chu"));
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
            XMLStreamReader reader = inputFactory.createXMLStreamReader(new StringReader(htmlSource));
            ParserHelper fragmentParser = new ParserHelper(reader);
            ParserHelper detailParser = new ParserHelper(reader);
            ParserHelper attributeParser = new ParserHelper(reader);
            ParserHelper.writeToFile(htmlSource);
            int eventType;
            while (reader.hasNext()) {
                eventType = reader.next();
                if (eventType == START_ELEMENT) {
                    if (isTag("div", reader) && equalClasses(reader, "", "bookdetail")) {
                        book = new Book(url);
                        fragmentParser.mark();
                        try {
                            //get image
                            fragmentParser.skipTo("img");
                            String imageUrl = reader.getAttributeValue("", "src");
                            book.setImageUrl(imageUrl);
                            //get title
                            fragmentParser.skipToWithClassName("div", "info");
                            fragmentParser.mark();
                            fragmentParser.skipToCharacter();
                            String title = fragmentParser.readTextInside();
                            book.setTitle(title != null ? title.replace("\n", "") : null);
                            //get intro
                            fragmentParser.skipToWithClassName("div", "intro");
                            fragmentParser.mark();
                            //get attributes
                            fragmentParser.skipToWithClassName("div", "attributes");
                            detailParser.mark();
                            StringBuilder sb = new StringBuilder();
                            while (true) {
                                String key = null;
                                String value = null;
                                try {
                                    detailParser.skipToWithClassName("li", "dataattr");
                                } catch (BoundReachedException e) {
                                    //end attribute
                                    break;
                                }
                                try {
                                    attributeParser.mark();
                                    //get key
                                    attributeParser.skipToCharacter();
                                    key = attributeParser.readTextInside();
                                    //get value
                                    sb = new StringBuilder();
                                    attributeParser.skipToBound(sb);
                                } catch (BoundReachedException e) {
                                    e.printStackTrace();
                                } finally {
                                    //end attribute
                                    detailParser.addCounter(-1);
                                }
                                value = sb.toString();
                                identifier.indentify(key != null ? key.trim() : null,
                                        value != null ? value.trim() : null);
                            }
                            //get price
                            fragmentParser.skipToWithClassName("p", "oldprice");
                            fragmentParser.mark();
                            fragmentParser.skipTo("span");
                            Double price = parseDouble(fragmentParser.readTextInside()).orElse(null);
                            book.setPrice(price);

                            //add attribute to object
                            Map<String, String> values = identifier.values();
                            String id = values.get("ID");
                            String author = values.get("AUTHOR");
                            String translator = values.get("TRANSLATOR");
                            Integer pageNumber = parseInt(values.get("PAGE_NUMBER")).orElse(null);
                            String size = values.get("SIZE");

                            book.setId("nxbnhanam-" + id);
                            book.setAuthor(author != null ? author.replace("\n", "") : null);
                            book.setTranslator(translator != null ? translator.replace("\n", "") : null);
                            book.setPageNumber(pageNumber);
                            book.setPageSize(size);
                        } catch (BoundReachedException e) {
                        }
                    } else if (isTag("div", reader) && equalClasses(reader, "", "bookdetailblockcontent")) {
                        fragmentParser.mark();
                        try {
                            fragmentParser.skipTo("article");
                            fragmentParser.mark();
                            StringBuilder sb = new StringBuilder();
                            fragmentParser.skipToBound(sb);
                            String description = sb.toString();
                            book.setDescription(description);
//                            System.out.println(description);
                        } catch (BoundReachedException e) {
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return book;
    }

    public List<String> crawlNewBookUrls(String url) {
        List<String> rs = new ArrayList<>();
        try {
            String htmlSource = crawl(url);
            if (htmlSource != null) {
                StringBuilder sb = new StringBuilder();
                NestedTagResolver.formatNestedTag(htmlSource, "html").forEach(sb::append);
                htmlSource = sb.toString();
            }

            XMLStreamReader reader = inputFactory.createXMLStreamReader(new StringReader(htmlSource));
            ParserHelper fragmentParser = new ParserHelper(reader);
//            ParserHelper.writeToFile(htmlSource);
            String bookUrl;
            int eventType;
            while (reader.hasNext()) {
                eventType = reader.next();
                if (eventType == START_ELEMENT && isTag("li", reader) && equalClasses(reader, "", "book")) {
                    fragmentParser.mark();
                    try {
                        fragmentParser.skipTo("a");
                        bookUrl = reader.getAttributeValue("", "href");
                        rs.add(bookUrl);
                    } catch (BoundReachedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rs;
    }
}
