package namlt.xml.asm.prj.crawler;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import namlt.xml.asm.prj.parser.BaseParser;
import namlt.xml.asm.prj.parser.NestedTagResolver;
import namlt.xml.asm.prj.parser.ParserHelper;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;
import namlt.xml.asm.prj.model.Book;
import namlt.xml.asm.prj.parser.BoundReachedException;
import static namlt.xml.asm.prj.utils.CommonUtils.parseInt;
import static namlt.xml.asm.prj.utils.CommonUtils.parseDouble;
import static namlt.xml.asm.prj.utils.InternetUtils.crawl;

public class NhaNamCrawler extends BaseParser implements BookCrawler {

    private XMLInputFactory inputFactory = XMLInputFactory.newFactory();

    public NhaNamCrawler() {
        inputFactory.setProperty(
                XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
        inputFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
    }

    public static void main(String[] args) {
        NhaNamCrawler crawler = new NhaNamCrawler();
//        crawler.crawlNewBookUrls("http://nhanam.com.vn/sach-moi-xuat-ban?page=1").forEach(System.out::println);
//        System.out.println(crawler.crawlBookPage("http://nhanam.com.vn/sach/16684/gau-a-cau-on-chu"));
//        List<Book> books = crawler.crawlNextNewBooks(0, 20);
        List<Book> books = crawler.search("Mẹ");
        System.out.println("Book number:" + books.size());
        books.forEach(System.out::println);
    }

    @Override
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
                                    detailParser.skipTo("li");
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
                                if (isExceptionAttribute(key)) {
                                    value = exceptionAttributeValue(key);
                                    key = identifyExceptionAttributeKey(key);
                                }
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

                            book.setId(id);
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

    @Override
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

    @Override
    public List<Book> crawlNextNewBooks(int start, int time) {
        List<Book> rs = new ArrayList<>();
        String tmp = "http://nhanam.com.vn/sach-moi-xuat-ban?page=";
        List<String> urls = new ArrayList<>();
        if (start <= 0) {
            start = 1;
        }
        for (int i = start; i < time; i++) {
            String url = tmp + (i + 1);
            crawlNewBookUrls(url).forEach(s -> urls.add("http://nhanam.com.vn" + s));
        }
        urls.parallelStream().map(s -> crawlBookPage(s))
                .filter(b -> {
                    if (!validateData(b)) {
                        return false;
                    }
                    //generate id
                    String id = generateId(b);
                    if (id == null) {
                        return false;
                    } else {
                        b.setId(id);
                    }
                    return true;
                })
                .forEach(b -> {
                    synchronized (rs) {
                        rs.add(b);
                    }
                });
//        System.out.println("URL:" + urls.size());
        return rs;
    }

    @Override
    public List<Book> search(String s) {
        List<Book> rs = new ArrayList<>();
        String tmp = null;
        try {
            tmp = "http://nhanam.com.vn/tim-kiem?q=" + URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(NxbTreCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<String> urls = new ArrayList<>();
        crawlNewBookUrls(tmp).forEach(u -> urls.add("http://nhanam.com.vn" + u));
        urls.parallelStream().map(u -> crawlBookPage(u))
                .filter(b -> {
                    if (!validateData(b)) {
                        return false;
                    }
                    //generate id
                    String id = generateId(b);
                    if (id == null) {
                        return false;
                    } else {
                        b.setId(id);
                    }
                    return true;
                })
                .forEach(b -> {
                    synchronized (rs) {
                        rs.add(b);
                    }
                });
        return rs;
    }

    private boolean isExceptionAttribute(String key) {
        if (key.contains("Số trang") || key.contains("Kích thước")) {
            return true;
        }
        return false;
    }

    private String identifyExceptionAttributeKey(String key) {
        if (key.contains("Số trang:")) {
            return "Số trang:";
        } else if (key.contains("Kích thước:")) {
            return "Kích thước:";
        }
        return null;
    }

    private String exceptionAttributeValue(String s) {
        if (s != null) {
            if (s.contains("Số trang")) {
                return s;
            } else if (s.contains("Kích thước:")) {
                return s.replace("Kích thước:", "").trim();
            }
            return null;
        }
        return null;
    }

    @Override
    public String generateId(Book b) {
        if (b.getId() == null) {
            return null;
        }
        return "nxbnhanam-" + b.getId();
    }
}
